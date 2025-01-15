package com.solution.calc.openapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.calc.api.money.dto.DepositExcelDto;
import com.solution.calc.api.money.dto.DepositExcelSearchRequestDto;
import com.solution.calc.constant.ErrorCode;
import com.solution.calc.domain.money.service.CalculateService;
import com.solution.calc.domain.money.service.DepositService;
import com.solution.calc.domain.rtpay.entity.RtpayKey;
import com.solution.calc.domain.rtpay.service.RtpayKeyService;
import com.solution.calc.domain.user.service.UserService;
import com.solution.calc.dto.ResponseDto;
import com.solution.calc.dto.Result;
import com.solution.calc.exception.ServiceLogicException;
import com.solution.calc.openapi.dto.AutoDepositRequestDto;
import com.solution.calc.openapi.dto.LoginApiResponseDto;
import com.solution.calc.openapi.dto.LoginDto;
import com.solution.calc.openapi.dto.TokenApiResponseDto;
import com.solution.calc.utils.BotHttpUtils;
import com.solution.calc.utils.ExcelUtils;
import com.solution.calc.utils.WebUtils;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/open-api")
@RequiredArgsConstructor
@Slf4j
public class OpenApiController implements OpenApiControllerIfs{

    private final UserService userService;

    private final DepositService depositService;

    private final ExcelUtils excelUtils;

    private final RtpayKeyService rtpayKeyService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<?> login(
            LoginDto loginDto
    ) {
        LoginApiResponseDto loginResponseDto = userService.userLogin(loginDto);
        ResponseDto<LoginApiResponseDto> response = ResponseDto.of(loginResponseDto, Result.ok());
        return ResponseEntity.ok().body(response);
    }

    @Override
    @PostMapping("/token")
    public ResponseEntity<?> loginApi(LoginDto loginDto) {
        TokenApiResponseDto loginResponseDto = userService.apiLogin(loginDto);
        ResponseDto<TokenApiResponseDto> response = ResponseDto.of(loginResponseDto, Result.ok());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/rtpay/basic/v2")
    @Hidden
    public ResponseEntity<?> rtpayBasicV2(HttpServletRequest request) {
        JSONObject jobj = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        String rtPay = "";

        String regPkey = request.getParameter("regPkey");
        log.info("regPkey = {}", regPkey);
        Optional<RtpayKey> opOffice = rtpayKeyService.getOpOffice(regPkey);
        rtPay = getPay(regPkey, jobj, request);

        try {
            /*`
             * {
             *  RCODE=200,
             *  MNO=0,
             *  RPAY=1000,
             *  RNAME=홍길동,
             *  RTEXT=입출금내역 알림 [입금] 1,000원 홍길동 114-******-04-050 10/10 17:21,
             *  RBANK=신한은행,
             *  RNUMBER=114-******-04-050
             * }
             * */
            Map map = mapper.readValue(rtPay, Map.class);
            opOffice.ifPresent(rtpayKey -> map.put("office", rtpayKey.getOfficeUsername()));
            log.info("@@@@ RTPAY RESPONSE BASIC V2= {}", map);
            depositService.setDepositCompleteForBasic(map);
            return ResponseEntity.ok(Map.of(
                    "RCODE","200",
                    "PCHK", "OK"
            ));
        } catch (Exception e) {
            log.error("@@@@ RTPAY ERROR BASIC V2 = {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }


    @PostMapping("/rtpay/v2")
    @Hidden
    public ResponseEntity<?> rtpayV2(HttpServletRequest request) {
        JSONObject jobj = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        String rtPay = "";

        String regPkey = request.getParameter("regPkey");
        log.info("regPkey = {}", regPkey);
        Optional<RtpayKey> opOffice = rtpayKeyService.getOpOffice(regPkey);
        rtPay = getPay(regPkey, jobj, request);

        try {
            /*`
             * {
             *  RCODE=200,
             *  MNO=0,
             *  RPAY=1000,
             *  RNAME=홍길동,
             *  RTEXT=입출금내역 알림 [입금] 1,000원 홍길동 114-******-04-050 10/10 17:21,
             *  RBANK=신한은행,
             *  RNUMBER=114-******-04-050
             * }
             * */
            Map map = mapper.readValue(rtPay, Map.class);
            if (opOffice.isPresent()) {
                map.put("office", opOffice.get().getOfficeUsername());
            }
            log.info("@@@@ RTPAY RESPONSE V2= {}", map);
            depositService.setDepositComplete(map);
            return ResponseEntity.ok(Map.of(
                    "RCODE","200",
                    "PCHK", "OK"
            ));
        } catch (Exception e) {
            log.error("@@@@ RTPAY ERROR V2 = {}", e.getMessage());
            return ResponseEntity.badRequest().build();

        }

    }
    @PostMapping("/rtpay/v2/auto")
    @Hidden
    public ResponseEntity<?> rtpayV2Auto(HttpServletRequest request) {
        JSONObject jobj = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        String rtPay = "";

        String regPkey = request.getParameter("regPkey");
        log.info("regPkey = {}", regPkey);
        String office = rtpayKeyService.getOffice(regPkey);
        rtPay = getPay(regPkey, jobj, request);

        try {
            /*`
             * {
             *  RCODE=200,
             *  MNO=0,
             *  RPAY=1000,
             *  RNAME=홍길동,
             *  RTEXT=입출금내역 알림 [입금] 1,000원 홍길동 114-******-04-050 10/10 17:21,
             *  RBANK=신한은행,
             *  RNUMBER=114-******-04-050
             * }
             * */
            Map map = mapper.readValue(rtPay, Map.class);
            map.put("office", office);
            log.info("@@@@ RTPAY RESPONSE V2= {}", map);
            depositService.autoDeposit(map);
        } catch (Exception e) {
            log.error("@@@@ RTPAY ERROR V2 = {}", e.getMessage());

        }
        return ResponseEntity.ok(Map.of(
                "RCODE","200",
                "PCHK", "OK"
        ));
    }

    @PostMapping("/deposit/auto")
    @Hidden
    public ResponseEntity<?> depositAuto(@RequestBody AutoDepositRequestDto requestDto) {
        userService.verifyUser(requestDto);
        boolean result = depositService.autoDeposit(requestDto);
        if (!result) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }


    public String getPay(String RTP_KEY, JSONObject jobj, HttpServletRequest request) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            log.info("### call RTPAY");
            String ugrd = request.getParameter("ugrd");
            String RTP_URL = "https://rtpay.net/CheckPay/checkpay.php";
            String data = "";
            String name = "";
            String retStr = "";
            if ("11".equals(ugrd)||"12".equals(ugrd)) RTP_URL="https://rtpay.net/CheckPay/test_checkpay.php";
            Enumeration enums = request.getParameterNames();
            while(enums.hasMoreElements()) {
                name = (String)enums.nextElement();
                if (!data.equals("")) data+="&";
                data += name+"="+ URLEncoder.encode(request.getParameter(name));
            }

            if ("".equals(data)) {
                data="regPkey="+RTP_KEY+"&Referer="+request.getRequestURL();
                RTP_URL="https://rtpay.net/CheckPay/setPurl.php";
            }
            HttpPost httpPost = new HttpPost(RTP_URL+"?"+data);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            log.info("Executing request = {}", httpPost.getRequestLine());
            CloseableHttpResponse execute = httpclient.execute(httpPost);
            int statusCode = execute.getStatusLine().getStatusCode();
            if (statusCode == 409) {
                return null;
            } else {
                String response = EntityUtils.toString(execute.getEntity());
                return response;
            }

        } catch (IOException e) {
            log.error("### call RTPAY INTERNAL_SERVER_ERROR");
            throw new ServiceLogicException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    @GetMapping("/excel")
    public void excelDownLoad(DepositExcelSearchRequestDto requestDto,  HttpServletResponse response) {
        log.info("/excel 요청");
        // 엑셀로 출력할 입금 리스트 조회
        List<DepositExcelDto> list = depositService.findAllDeposit(requestDto, requestDto.getUserId(), requestDto.getUserLevel());

        // 엑셀 다운로드 로직 실행
        excelUtils.depositExcelDownload(list, response);
    }


}

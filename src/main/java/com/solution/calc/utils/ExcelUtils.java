package com.solution.calc.utils;

import com.solution.calc.annotation.ExcelColumn;
import com.solution.calc.api.money.dto.DepositExcelDto;
import com.solution.calc.constant.DepositStatus;
import com.solution.calc.constant.ErrorCode;
import com.solution.calc.exception.ServiceLogicException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ExcelUtils implements ExcelUtilMethodFactory {
    @Override
    public void depositExcelDownload(List<DepositExcelDto> data, HttpServletResponse response) {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("First Sheet");

        Cell cell = null;
        Row row = null;
        // List가 아닌 DTO를 넘겨줘야 하므로 메서드를 통해 DTO의 class 정보가 담긴 class 객체를 넣어준다.
        // Header의 내용을 List로 반환 받는다(엑셀의 Cell의 첫줄이 된다.)
        Class<?> aClass = getClass(data);
        List<String> excelHeaderList = List.of();
        if (aClass == null) {
            excelHeaderList =getHeaderName(Objects.requireNonNull(getClass(List.of(DepositExcelDto.builder().build()))));
            row = sheet.createRow(0);
            for (int i = 0; i < excelHeaderList.size(); i++) {
                sheet.setColumnWidth(i, 6000);
                // 열을 만들어준다.
                cell = row.createCell(i);

                // 열에 헤더이름(컬럼 이름)을 넣어준다.
                cell.setCellValue(excelHeaderList.get(i));

            }

        } else {
            excelHeaderList = getHeaderName(aClass);
            // 첫 행을 생성해준다.
            row = sheet.createRow(0);

            // 헤더의 수(컬럼 이름의 수)만큼 반복해서 행을 생성한다.
            for (int i = 0; i < excelHeaderList.size(); i++) {
                sheet.setColumnWidth(i, 6000);
                // 열을 만들어준다.
                cell = row.createCell(i);

                // 열에 헤더이름(컬럼 이름)을 넣어준다.
                cell.setCellValue(excelHeaderList.get(i));

            }
            renderStudentExcelBody(data, sheet, row, cell);
        }


        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=DepositList.xlsx");

        try {
            // 엑셀 파일을 다운로드 하기 위해 write() 메서드를 사용한다.
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            // checked 예외를 사용하면 추후 의존이나 예외 누수 문제가 생길 수 있으므로
            // RuntimeException으로 한번 감싸서, cause가 나올 수 있게 발생한 예외를 넣어준다.
            log.error("Workbook write 수행 중 IOException 발생!");
            throw new ServiceLogicException(ErrorCode.EXCEL_IO_ERROR);
        } finally {
            // 파일 입출력 스트림을 사용한 후에는 예외 발생 여부와 관계없이 반드시 닫아 주어야 한다.
            closeWorkBook(workbook);
        }
    }

    @Override
    public void renderStudentExcelBody(List<DepositExcelDto> data, Sheet sheet, Row row, Cell cell) {
        // 현재 행의 개수를 가지고 있는 변수 rowCount 선언(Header를 그리고 시작했으므로 1부터 시작)
        int rowCount = 1;

        // 조회해온 데이터 리스트(List<StudentDto>)의 크기만큼 반복문을 실행한다.
        for (DepositExcelDto depositDate : data) {

            // 헤더를 설정할때 0번 인덱스가 사용 되었으므로, i값에 1을 더해서 1번 로우(행)부터 생성한다.
            row = sheet.createRow(rowCount++);
            List<String> excelHeaderList = getHeaderName(getClass(data));
            for (int i = 0; i < excelHeaderList.size(); i++) {

                // 열을 만들어준다.
                cell = row.createCell(i);

                cell.setCellValue(getValue(i, depositDate));

            }
        }
    }

    private Class<?> getClass(List<?> data) {
        // List가 비어있지 않다면 List가 가지고 있는 모든 DTO는 같은 필드를 가지고 있으므로,
        // 맨 마지막 DTO만 빼서 클래스 정보를 반환한다.
        if (!CollectionUtils.isEmpty(data)) {
            return data.get(data.size() - 1).getClass();
        } else {
            log.info("리스트가 비어 있음!");
            return null;
        }
    }

    private List<String> getHeaderName(Class<?> type) {

        // 스트림으로 엑셀 헤더 이름들을 리스트로 반환
        // 1. 매개변수로 전달된 클래스의 필드들을 배열로 받아, 스트림을 생성
        // 2. @ExcelColumn 애너테이션이 붙은 필드만 수집
        // 3. @ExcelColumn 애너테이션이 붙은 필드에서 애너테이션의 값을 매핑
        // 4. LinkedList로 반환
        List<String> excelHeaderNameList = Arrays.stream(type.getDeclaredFields())
                .filter(s -> s.isAnnotationPresent(ExcelColumn.class))
                .map(s -> s.getAnnotation(ExcelColumn.class).headerName())
                .collect(Collectors.toCollection(LinkedList::new));

        // 헤더의 이름을 담은 List가 비어있을 경우, 헤더 이름이 지정되지 않은 것이므로, 예외를 발생시킨다.
        if (CollectionUtils.isEmpty(excelHeaderNameList)) {
            log.error("헤더 이름이 조회되지 않아 예외 발생!");
            throw new IllegalStateException("헤더 이름이 없습니다.");
        }
        return excelHeaderNameList;
    }

    private void closeWorkBook(Workbook workbook) {
        try {
            if(workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            // checked 예외를 사용하면 추후 의존이나 예외 누수 문제가 생길 수 있으므로
            // RuntimeException으로 한번 감싸서, cause가 나올 수 있게 발생한 예외를 넣어준다.
            throw new RuntimeException(e);
        }
    }

    private String getValue(int num, DepositExcelDto dto) {
        if (num == 0) {
            return dto.getDataId().toString();
        } else if (num == 1) {
            return dto.getOfficeUsername();
        } else if (num == 2) {
            return dto.getOfficeNickName();
        } else if (num == 3) {
            return dto.getTopAdminNickName();
        } else if (num == 4) {
            return dto.getBasicUsername();
        } else if (num == 5) {
            return dto.getBasicUserNickName();
        } else if (num == 6) {
            return dto.getDepositBank();
        } else if (num == 7) {
            return dto.getDepositAccount();
        } else if (num == 8) {
            return dto.getTxnId();
        } else if (num == 9) {
            return dto.getDepositStatus().getStatus();
        } else if (num == 10) {
            BigDecimal depositBalance = dto.getDepositBalance();
            DecimalFormat df = new DecimalFormat("###,###,###");
            return df.format(depositBalance);
        } else if (num == 11) {
            LocalDateTime completeAt = dto.getCompleteAt();
            if (completeAt != null) {
                return completeAt.toString();
            }
            return "null";
        } else if (num == 12) {
            return dto.getCreateAt().toString();
        }
        throw new ServiceLogicException(ErrorCode.BAD_REQUEST);
    }
}

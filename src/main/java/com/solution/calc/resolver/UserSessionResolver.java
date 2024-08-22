package com.solution.calc.resolver;

import com.solution.calc.annotation.UserSession;
import com.solution.calc.constant.ErrorCode;
import com.solution.calc.domain.user.entity.User;
import com.solution.calc.domain.user.repository.UserRepository;
import com.solution.calc.exception.ServiceLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class UserSessionResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 지원하는 파라미터 , 어노테이션 체크
        // AOP 방식으로 실행하기 위한 리졸버
        boolean annotation = parameter.hasParameterAnnotation(UserSession.class);
        boolean parameterType = parameter.getParameterType().equals(User.class);

        return annotation && parameterType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        // support parameter 에서 true 반환시 여기 실행
        // JwtAuthorizationFilter 에서 Context에 userId 넣어둠
        // 사용자 정보 셋팅
        RequestAttributes requestContext = RequestContextHolder.getRequestAttributes();
        Object userId = requestContext.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        User user = userRepository.findUserByUserId(Long.parseLong(userId.toString()))
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND));
        User setUser = User.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .userStatus(user.getUserStatus())
                .userLevel(user.getUserLevel())
                .balance(user.getBalance())
                .roles(user.getRoles())
                .agent1Id(user.getAgent1Id())
                .agent2Id(user.getAgent2Id())
                .agent3Id(user.getAgent3Id())
                .build();
        setUser.setCreateAt(user.getCreateAt());
        return setUser;
    }
}
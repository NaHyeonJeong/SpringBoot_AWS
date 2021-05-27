package com.hj.springaws.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @EnableJpaAuditing과 @SpringBootApplication을 분리하기 위해
 * 클래스를 따로 만들었다
 * @WebMvcTest는 일반적인 @Configuration은 스캔하지 않음
 */
@Configuration
@EnableJpaAuditing //JPA Auditing 활성화
public class JpaConfig {
}

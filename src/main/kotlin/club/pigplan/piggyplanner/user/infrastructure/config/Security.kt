package club.pigplan.piggyplanner.user.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.security.crypto.factory.PasswordEncoderFactories
//import org.springframework.security.crypto.password.PasswordEncoder


//@Configuration
//class InMemoryAuthWebSecurityConfigurer : WebSecurityConfigurerAdapter() {
//    override fun configure(auth: AuthenticationManagerBuilder) {
//        val encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
//        auth.inMemoryAuthentication()
//                .withUser("user1")
//                .password(encoder.encode("secret"))
//                .roles("USER")
//    }
//}
//@Bean
//fun passwordEncoder(): PasswordEncoder {
//    return BCryptPasswordEncoder()
//}
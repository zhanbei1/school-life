package com.school.config;/**
 * Created by zhanbei on 2020/8/26.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

/**
 * @ClassName Oauth2Config
 * @Description
 * @Author zhanbei
 * @Date 2020/8/26 23:41
 * @Version 1.0
 **/
@Configuration
public class Oauth2Config {
    /**
     *认证服务器
    **/
    public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter{
        //注入自定义用户信息服务，基于内存，
        @Autowired
        private UserDetailsService userDetailsService;

        //数据源
        @Qualifier("dataSource")
        @Autowired
        private DataSource dataSource;

        //认证管理器，开启密码和授权
        @Autowired
        private AuthenticationManager authenticationManager;

        //jwtToken增强，自定义实现token扩展
        @Autowired
        private TokenEnhancer tokenEnhancer;


        //token存储，自定义保存到数据库中
        @Autowired
        private TokenStore tokenStore;

        //token转换器
        @Autowired
        private JwtAccessTokenConverter jwtAccessTokenConverter;

        //客户端信息来源于DB
        @Bean
        public JdbcClientDetailsService clientDetailsService(){
            return new JdbcClientDetailsService(dataSource);
        }

        //自定义将授权保存到数据库中
        @Bean
        public ApprovalStore approvalStore(){
            return new JdbcApprovalStore(dataSource);
        }

        //将授权码保存到数据库
        @Bean
        public AuthorizationCodeServices authorizationCodeServices(){
            return new JdbcAuthorizationCodeServices(dataSource);
        }

        //配置令牌端点（Token Endpoint）的安全约束
        //授权服务安全配置，单体应用注意配置checkTokenAccess放行
        @Override
        public void configure(AuthorizationServerSecurityConfigurer security ) throws  Exception{
            super.configure(security);
           //允许oauth,token调用
            security.tokenKeyAccess("permitAll()")
                    //.checkTokenAccess("permitAll()");
                    .checkTokenAccess("isAuthenticated");
        }

        //配置客户端详情服务（clientDetailService）
        //客户端详情信息在这里初始化
        //通过数据库来存储调用详情信息
        //配置客户，但详情从数据库读取，默认手动添加oauth2客户端表中数据
        public ClientDetailsService jdbcClientDetailes(){
            return new JdbcClientDetailsService(dataSource);
        }
    }
}
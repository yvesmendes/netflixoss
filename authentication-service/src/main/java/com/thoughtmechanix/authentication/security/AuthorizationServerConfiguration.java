package com.thoughtmechanix.authentication.security;

import java.security.KeyPair;
import java.time.Duration;
import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(SecurityProperties.class)
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Value("${redis.host}")
	private String redisHost;

	@Value("${redis.port}")
	private int redisPort;

	private final DataSource dataSource;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final com.thoughtmechanix.authentication.security.SecurityProperties securityProperties;
	private final UserDetailsService userDetailsService;
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	private TokenStore tokenStore;

	public AuthorizationServerConfiguration(final DataSource dataSource, final PasswordEncoder passwordEncoder,
			final AuthenticationManager authenticationManager,
			final com.thoughtmechanix.authentication.security.SecurityProperties securityProperties,
			final UserDetailsService userDetailsService) {
		this.dataSource = dataSource;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.securityProperties = securityProperties;
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public JedisConnectionFactory redisConnectionFactory() {

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost,
				redisPort);
		// redisStandaloneConfiguration.setPassword(RedisPassword.of("yourRedisPasswordIfAny"));

		JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
		jedisClientConfiguration.connectTimeout(Duration.ofSeconds(60));// 60s connection timeout

		return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration.build());
	}

	@Bean
	public TokenStore tokenStore() {
		if (tokenStore == null) {
			tokenStore = new RedisTokenStore(redisConnectionFactory());
		}

		return tokenStore;
	}

	@Bean
	public AuthorizationServerTokenServices tokenServices(final TokenStore tokenStore,
			final ClientDetailsService clientDetailsService) {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setTokenStore(tokenStore);
		tokenServices.setClientDetailsService(clientDetailsService);
		tokenServices.setAuthenticationManager(this.authenticationManager);
		tokenServices.setTokenEnhancer(jwtTokenEnhancer());
		return tokenServices;
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {

		if (jwtAccessTokenConverter != null) {
			return jwtAccessTokenConverter;
		}

		com.thoughtmechanix.authentication.security.SecurityProperties.JwtProperties jwtProperties = securityProperties
				.getJwt();

		KeyPair keyPair = keyPair(jwtProperties, keyStoreKeyFactory(jwtProperties));

		jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setKeyPair(keyPair);
		return jwtAccessTokenConverter;
	}

	@Override
	public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
		clients.jdbc(this.dataSource);
	}

	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenEnhancer(), jwtAccessTokenConverter()));

		endpoints.authenticationManager(this.authenticationManager).tokenEnhancer(tokenEnhancerChain)
				.userDetailsService(this.userDetailsService).tokenStore(tokenStore()).reuseRefreshTokens(false);
	}

	@Override
	public void configure(final AuthorizationServerSecurityConfigurer oauthServer) {
		oauthServer.passwordEncoder(this.passwordEncoder).tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()");
	}

	private KeyPair keyPair(com.thoughtmechanix.authentication.security.SecurityProperties.JwtProperties jwtProperties,
			KeyStoreKeyFactory keyStoreKeyFactory) {
		return keyStoreKeyFactory.getKeyPair(jwtProperties.getKeyPairAlias(),
				jwtProperties.getKeyPairPassword().toCharArray());
	}

	private KeyStoreKeyFactory keyStoreKeyFactory(
			com.thoughtmechanix.authentication.security.SecurityProperties.JwtProperties jwtProperties) {
		return new KeyStoreKeyFactory(jwtProperties.getKeyStore(), jwtProperties.getKeyStorePassword().toCharArray());
	}

	@Bean
	public TokenEnhancer jwtTokenEnhancer() {
		return new JWTTokenEnhancer();
	}
}
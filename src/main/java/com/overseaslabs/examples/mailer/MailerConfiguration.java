package com.overseaslabs.examples.mailer;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@PropertySource("application.properties")
@EnableJpaAuditing
class MailerConfiguration {

    /**
     * Redis host
     */
    @Value("${spring.redis.host}")
    private String host;

    /**
     * Redis port
     */
    @Value("${spring.redis.port}")
    private Integer port;

    /**
     * Sendgrid API key
     */
    @Value("${SENDGRID_API_KEY}")
    private String sgApiKey;

    @Bean()
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return template;
    }

    /**
     * The topic for publishing user registry messages
     */
    @Bean("ureg")
    ChannelTopic uregTopic() {
        return new ChannelTopic("example:ureg");
    }

    /**
     * The topic for publishing web messages
     */
    @Bean("web")
    ChannelTopic webTopic() {
        return new ChannelTopic("example:web");
    }

    @Bean
    MessageListener messageListener() {
        return new RedisMessageListener();
    }

    @Bean
    RedisMessageListenerContainer container() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(), uregTopic());

        return container;
    }

    /**
     * Sendgrid API client
     */
    @Bean
    SendGrid sendGrid() {
        return new SendGrid(sgApiKey);
    }
}
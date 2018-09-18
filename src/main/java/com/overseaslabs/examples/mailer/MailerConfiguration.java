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
public class MailerConfiguration {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private Integer port;

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
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return template;
    }

    @Bean("ureg")
    ChannelTopic uregTopic() {
        return new ChannelTopic("example:ureg");
    }

    @Bean("web")
    ChannelTopic webTopic() {
        return new ChannelTopic("example:web");
    }

    @Bean
    MessageListener messageListener() {
        return new RedisMessageListener();
    }

    @Bean
    public RedisMessageListenerContainer container() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(), uregTopic());

        return container;
    }

    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(sgApiKey);
    }
}
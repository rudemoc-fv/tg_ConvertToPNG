package com.rudemoc.convert.config;

import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@Data
public class BotConfig {
    public String botName = "convertdotbot";
    public String token = "";
}
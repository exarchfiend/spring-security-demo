package fun.mjauto.auth.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author MJ
 * @description 生成验证码工具Kaptcha配置类
 * @date 22/10/2023
 */
@Configuration
public class KaptchaConfig {

    private final Properties properties;
    private final static String WIDTH = "120";
    private final static String HEIGHT = "40";
    private final static String CHAR_STRING = "0123456789";
    private final static String CHAR_LENGTH = "4";

    public KaptchaConfig() {
        this.properties = new Properties();
    }

    @Bean
    public Producer producer() {
        // 创建DefaultKaptcha对象
        DefaultKaptcha captcha = new DefaultKaptcha();
        // 图片宽
        this.properties.setProperty("kaptcha.image.width", WIDTH);
        // 图片高
        this.properties.setProperty("kaptcha.image.height", HEIGHT);
        //文本集合，验证码值从此集合中获取
        this.properties.setProperty("kaptcha.textproducer.char.string", CHAR_STRING);
        //验证码长度
        this.properties.setProperty("kaptcha.textproducer.char.length", CHAR_LENGTH);
        setInterfere();
        Config config = new Config(this.properties);
        captcha.setConfig(config);
        return captcha;
    }

    private void setInterfere() {
        //干扰实现类
        this.properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
        //干扰颜色
        this.properties.setProperty("kaptcha.noise.color", "blue");
        //干扰图片样式
        this.properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");
        //背景实现类
        this.properties.setProperty("kaptcha.background.impl", "com.google.code.kaptcha.impl.DefaultBackground");
        //背景颜色渐变，结束颜色
        this.properties.setProperty("kaptcha.background.clear.to", "white");
        //文字渲染器
        this.properties.setProperty("kaptcha.word.impl", "com.google.code.kaptcha.text.impl.DefaultWordRenderer");
    }

    private void setOther() {
        // 图片边框
        this.properties.setProperty("kaptcha.border", "no");
        // 边框颜色
        this.properties.setProperty("kaptcha.border.color", "black");
        //边框厚度
        this.properties.setProperty("kaptcha.border.thickness", "1");

        //图片实现类
        this.properties.setProperty("kaptcha.producer.impl", "com.google.code.kaptcha.impl.DefaultKaptcha");
        //文本实现类
        this.properties.setProperty("kaptcha.textproducer.impl", "com.google.code.kaptcha.text.impl.DefaultTextCreator");

        //文字间隔
        this.properties.setProperty("kaptcha.textproducer.char.space", "4");

        //字体
        this.properties.setProperty("kaptcha.textproducer.font.names", "宋体");
        //字体颜色
        this.properties.setProperty("kaptcha.textproducer.font.color", "black");
    }
}

package com.yolo.mrs.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;
import java.io.ByteArrayOutputStream;

import static com.yolo.mrs.utils.Constant.CODE_MAX;

public class CaptchaGenerator {

    public String getCaptcha() {
        // 验证码图片的高度和宽度
        int width = 160;
        int height = 60;

        // 创建一个BufferedImage对象
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 获取Graphics2D对象，用于在BufferedImage上绘图
        Graphics2D g2d = bufferedImage.createGraphics();

        // 设置背景色（这里可以使用渐变背景）
        GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, height, Color.LIGHT_GRAY, true);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        // 添加噪点
        addNoise(g2d, width, height, 50);

        // 添加干扰线
        addDisturbLines(g2d, width, height, 4);

        // 设置字体
        g2d.setFont(new Font("Arial", Font.BOLD, 32));

        // 生成四位数验证码
        Random random = new Random();
        StringBuilder captchaText = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            captchaText.append(random.nextInt(10)); // 生成0-9之间的随机数
        }
        System.out.println("captchaText = " + captchaText);

        // 设置验证码文字的颜色
        g2d.setColor(getRandomColor());

        // 在图片上绘制验证码文本
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int x = (width - fontMetrics.stringWidth(captchaText.toString()));
        int y = height / 2 + fontMetrics.getAscent() / 2 - 2;
        g2d.drawString(captchaText.toString(), x, y);
        String code = captchaText.toString();
        // 释放Graphics2D资源
        g2d.dispose();
        // 将BufferedImage转换为byte数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "PNG", baos);
            baos.flush();

            // 将byte数组转换为Base64字符串
            byte[] imageBytes = baos.toByteArray();
            String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

            // 返回Base64字符串
            return "data:image/png;base64," + imageBase64 + "@" + code;
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 或者抛出一个自定义的异常
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 生成随机颜色
    private static Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    // 添加噪点
    private static void addNoise(Graphics2D g2d, int width, int height, int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g2d.fillRect(x, y, 1, 1);
        }
    }

    // 添加干扰线
    private static void addDisturbLines(Graphics2D g2d, int width, int height, int count) {
        Random random = new Random();
        g2d.setColor(Color.GRAY);
        for (int i = 0; i < count; i++) {
            int x1 = random.nextInt(width);
            int x2 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int y2 = random.nextInt(height);
            g2d.drawLine(x1, y1, x2, y2);
        }
    }
}
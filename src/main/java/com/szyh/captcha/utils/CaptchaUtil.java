package com.szyh.captcha.utils;

import java.awt.Font;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.szyh.captcha.ArithmeticCaptcha;
import com.szyh.captcha.ChineseCaptcha;
import com.szyh.captcha.ChineseGifCaptcha;
import com.szyh.captcha.GifCaptcha;
import com.szyh.captcha.SpecCaptcha;
import com.szyh.captcha.base.Captcha;

/**
 * 图形验证码工具类
 */
public class CaptchaUtil {
    private static final String SESSION_KEY = "captcha";
    private static final int DEFAULT_LEN = 4; // 默认长度
    private static final int DEFAULT_WIDTH = 130; // 默认宽度
    private static final int DEFAULT_HEIGHT = 48; // 默认高度
    public static final String CAPTCHA_TYPE_ALPHANUMERIC = "alphanumeric"; // 静态（字母数字)
    public static final String CAPTCHA_TYPE_DYNAMIC_ALPHANUMERIC = "dynamicalphanumeric"; // 动态(字母数字)
    public static final String CAPTCHA_TYPE_FORMULA = "formula"; // 公式
    public static final String CAPTCHA_TYPE_SINOGRAM = "sinogram"; // 静态（汉字png）
    public static final String CAPTCHA_TYPE_DYNAMIC_SINOGRAM = "dynamicsinogram"; // 动态（汉字jpg）

    /**
     * 输出验证码
     *
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @throws IOException
     *             IO异常
     */
    public static void out(HttpServletRequest request, HttpServletResponse response, String captchaType)
        throws IOException {
        out(DEFAULT_LEN, request, response, captchaType);
    }

    /**
     * 输出验证码
     *
     * @param len
     *            长度
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @throws IOException
     *             IO异常
     */
    public static void out(int len, HttpServletRequest request, HttpServletResponse response, String captchaType)
        throws IOException {
        out(DEFAULT_WIDTH, DEFAULT_HEIGHT, len, request, response, captchaType);
    }

    /**
     * 输出验证码
     *
     * @param width
     *            宽度
     * @param height
     *            高度
     * @param len
     *            长度
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @throws IOException
     *             IO异常
     */
    public static void out(int width, int height, int len, HttpServletRequest request, HttpServletResponse response,
        String captchaType) throws IOException {
        out(width, height, len, null, request, response, captchaType);
    }

    /**
     * 输出验证码
     *
     * @param font
     *            字体
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @throws IOException
     *             IO异常
     */
    public static void out(Font font, HttpServletRequest request, HttpServletResponse response, String captchaType)
        throws IOException {
        out(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_LEN, font, request, response, captchaType);
    }

    /**
     * 输出验证码
     *
     * @param len
     *            长度
     * @param font
     *            字体
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @throws IOException
     *             IO异常
     */
    public static void out(int len, Font font, HttpServletRequest request, HttpServletResponse response,
        String captchaType) throws IOException {
        out(DEFAULT_WIDTH, DEFAULT_HEIGHT, len, font, request, response, captchaType);
    }

    /**
     * 输出验证码
     *
     * @param width
     *            宽度
     * @param height
     *            高度
     * @param len
     *            长度
     * @param font
     *            字体
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @throws IOException
     *             IO异常
     */
    public static void out(int width, int height, int len, Font font, HttpServletRequest request,
        HttpServletResponse response, String captchaType) throws IOException {

        if (captchaType.equals(CAPTCHA_TYPE_FORMULA)) {
            ArithmeticCaptcha specCaptcha = new ArithmeticCaptcha(width, height, len);
            if (font != null) {
                specCaptcha.setFont(font);
            }
            out(specCaptcha, request, response);
        } else if (captchaType.equals(CAPTCHA_TYPE_SINOGRAM)) {
            ChineseCaptcha chineseCaptcha = new ChineseCaptcha(width, height, len);
            if (font != null) {
                chineseCaptcha.setFont(font);
            }
            out(chineseCaptcha, request, response);
        } else if (captchaType.equals(CAPTCHA_TYPE_DYNAMIC_SINOGRAM)) {
            ChineseGifCaptcha chineseGifCaptcha = new ChineseGifCaptcha(width, height, len);
            if (font != null) {
                chineseGifCaptcha.setFont(font);
            }
            out(chineseGifCaptcha, request, response);
        } else if (captchaType.equals(CAPTCHA_TYPE_DYNAMIC_ALPHANUMERIC)) {
            GifCaptcha gifCaptcha = new GifCaptcha(width, height, len);
            if (font != null) {
                gifCaptcha.setFont(font);
            }
            out(gifCaptcha, request, response);
        } else {
            SpecCaptcha specCaptcha = new SpecCaptcha(width, height, len);
            if (font != null) {
                specCaptcha.setFont(font);
            }
            out(specCaptcha, request, response);
        }
    }

    /**
     * 输出验证码
     *
     * @param captcha
     *            Captcha
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @throws IOException
     *             IO异常
     */
    public static void out(Captcha captcha, HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        setHeader(response);
        request.getSession().setAttribute(SESSION_KEY, captcha.text().toLowerCase());
        captcha.out(response.getOutputStream());
    }

    /**
     * 验证验证码
     *
     * @param code
     *            用户输入的验证码
     * @param request
     *            HttpServletRequest
     * @return 是否正确
     */
    public static boolean ver(String code, HttpServletRequest request) {
        if (code != null) {
            String captcha = (String)request.getSession().getAttribute(SESSION_KEY);
            return code.trim().toLowerCase().equals(captcha);
        }
        return false;
    }

    /**
     * 清除session中的验证码
     *
     * @param request
     *            HttpServletRequest
     */
    public static void clear(HttpServletRequest request) {
        request.getSession().removeAttribute(SESSION_KEY);
    }

    /**
     * 设置相应头
     *
     * @param response
     *            HttpServletResponse
     */
    public static void setHeader(HttpServletResponse response) {
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    }

}

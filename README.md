# captcha
轻松搞定集成验证码，中文，字母，算式

调用Filter
public class CaptchaFilter implements Filter {
//初始化验证码类型
    private static final String[] captcha = {CaptchaUtil.CAPTCHA_TYPE_ALPHANUMERIC,
        CaptchaUtil.CAPTCHA_TYPE_DYNAMIC_ALPHANUMERIC, CaptchaUtil.CAPTCHA_TYPE_FORMULA,
        CaptchaUtil.CAPTCHA_TYPE_SINOGRAM, CaptchaUtil.CAPTCHA_TYPE_DYNAMIC_SINOGRAM};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
	//随机获取验证码类型
        int index = (int)(Math.random() * captcha.length);
        int defaultlen = 4;//默认验证码长度
        String capthaType = captcha[index];
        if (capthaType.equals(CaptchaUtil.CAPTCHA_TYPE_FORMULA)) {//公式默认是三因素运算
            defaultlen = 3;
        }
        CaptchaUtil.out(defaultlen, httpRequest, httpResponse, captcha[index]);
    }

    @Override
    public void destroy() {}

}

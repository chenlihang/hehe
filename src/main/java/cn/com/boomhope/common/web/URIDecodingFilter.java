package cn.com.boomhope.common.web;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * URI过滤器
 * 
 * @author huangdiwen
 */
public class URIDecodingFilter extends OncePerRequestFilter {

    private String encoding;

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        URIHttpServletRequestWrapper wrapper = new URIHttpServletRequestWrapper(request, this.encoding);
        filterChain.doFilter(wrapper, response);
    }
}

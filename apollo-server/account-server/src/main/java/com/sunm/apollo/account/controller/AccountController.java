package com.sunm.apollo.account.controller;

import com.sunm.apollo.account.ImportAccountVO;
import com.sunm.apollo.common.excel.ImportExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hujl on 2019/11/17.
 */
@RestController
@RequestMapping("account")
public class AccountController {

    public static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Value("${server.port}")
    private int port;

    /**
     * 测试feign  hystrix超时时间
     *
     * @param mills
     * @return
     */
    @GetMapping("timeout")
    public String timeout(@RequestParam("mills") int mills) {
        logger.info("[client服务-{}] [timeOut方法]收到请求,阻塞{}ms", port, mills);
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("[client服务-{}] [timeOut]返回请求", port);
        return String.format("client服务-%s 请求ok!!!", port);
    }

    /**
     * 导入excel表
     *
     * @version 1.0
     * @since 1.0
     */
    @PostMapping(path = "/importAccount")
    public void uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<ImportAccountVO> importAccountList = ImportExcelUtil.importExcel(ImportAccountVO.class, file);
            //可做持久化操作，现只打印观察
            for (ImportAccountVO importAccountVO : importAccountList) {
                logger.error(importAccountVO.toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 导出excel模版
     *
     * @version 1.0
     * @since 1.0
     */
    @PostMapping(path = "/downloadAccount")
    public void downloadEmployeeModel(HttpServletResponse response) throws IOException {
        OutputStream os = null;
        BufferedInputStream input = null;
        try {
            response.setContentType("application/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(("eeelist").getBytes("UTF-8"), "iso-8859-1") + ".xls");
            Map<Integer, String[]> paramMap = new HashMap<Integer, String[]>();
            //excel第三行为下拉选择框
            paramMap.put(2, new String[]{"man", "women"});
            input = new BufferedInputStream(ImportExcelUtil.excelModelbyClass(ImportAccountVO.class, paramMap, null));
            byte buffBytes[] = new byte[1024];
            os = response.getOutputStream();
            int read;
            while ((read = input.read(buffBytes)) != -1) {
                os.write(buffBytes, 0, read);
            }

        } catch (Exception e) {
            logger.error("downloadEmployeeModel() catch Exception ", e);
        } finally {
            os.flush();
            os.close();
            input.close();
        }
    }
}

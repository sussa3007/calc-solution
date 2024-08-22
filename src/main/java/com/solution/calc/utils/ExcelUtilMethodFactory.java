package com.solution.calc.utils;

import com.solution.calc.api.money.dto.DepositExcelDto;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface ExcelUtilMethodFactory {

    void depositExcelDownload(List<DepositExcelDto> data, HttpServletResponse response);
    void renderStudentExcelBody(List<DepositExcelDto> data, Sheet sheet, Row row, Cell cell);
}
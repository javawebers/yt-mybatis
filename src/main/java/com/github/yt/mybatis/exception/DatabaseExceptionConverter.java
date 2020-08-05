package com.github.yt.mybatis.exception;

import com.github.yt.commons.exception.BaseAccidentException;
import com.github.yt.commons.exception.BaseExceptionConverter;
import com.github.yt.mybatis.YtMybatisExceptionEnum;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;

/**
 * 参数校验异常处理器
 * @author liujiasheng
 */
@Component
public class DatabaseExceptionConverter implements BaseExceptionConverter {

    @Override
    public Throwable convertToBaseException(Throwable e) {
        if (e instanceof DuplicateKeyException) {
            // 数据库异常，记录已存在
            return new BaseAccidentException(YtMybatisExceptionEnum.CODE_21, e);
        } else if (e instanceof DataIntegrityViolationException) {
            // 数据库异常，数据完整性异常(字段不为空，数据长度限制等)
            return new BaseAccidentException(YtMybatisExceptionEnum.CODE_22, e);
        } else if (e instanceof BadSqlGrammarException) {
            // 数据库异常，脚本语法异常(字段不存在等)
            return new BaseAccidentException(YtMybatisExceptionEnum.CODE_23, e);
        } else if (e instanceof EmptyResultDataAccessException) {
            // 数据库异常，记录数不正确(findOne getOne 等)
            return new BaseAccidentException(YtMybatisExceptionEnum.CODE_24, e);
        }
        return e;
    }
}

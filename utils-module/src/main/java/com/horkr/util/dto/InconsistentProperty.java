package com.horkr.util.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 对比后不一致的数据结果
 *
 * @author llh
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InconsistentProperty implements Serializable {
    private static final long serialVersionUID = -2996016661081460639L;
    /**
     * 属性的key
     */
    private String key;

    /**
     * 原始数据
     */
    private String oldData;

    /**
     * 新数据
     */
    private String newData;
}

package com.simple.gateway.admin.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 出参返回的分页格式
 */
@Data
@NoArgsConstructor
public class Pager<T> {

    /**
     * 允许的最大页数
     */
    public static int MAX_PAGE_SIZE = 500;

    /**
     * 页码
     */
    private long pageNum;

    /**
     * 每页条目数
     */
    private long pageSize;

    /**
     * 总条目数
     */
    private long totalItems;

    /**
     * 总页数
     */
    private long totalPages;


    private List<T> data;


    public Pager(long pageNum, long pageSize, long totalItems, List<T> data) {
        if (pageSize <= 0) {
            pageSize = 10;
        }

        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = (long) Math.ceil(this.totalItems * 1.0 / this.pageSize);

        this.data = data;
    }


    public Pager(Pager pager) {
        this.pageNum = pager.getPageNum();
        this.pageSize = pager.getPageSize();
        this.totalItems = pager.getTotalItems();

        if (pageSize <= 0) {
            pageSize = 10;
        }

        this.totalPages = (long) Math.ceil(this.totalItems * 1.0 / this.pageSize);
    }

}

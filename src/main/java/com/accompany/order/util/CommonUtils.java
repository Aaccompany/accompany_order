package com.accompany.order.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Accompany
 * Date:2019/12/28
 */
public class CommonUtils {
    public static final Logger LOG = LoggerFactory.getLogger(CommonUtils.class);

    public CommonUtils() {
    }

    public static <T> T genByCopyProperties(Object source, Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (Exception var3) {
            LOG.error("genVo报错", var3);
            return null;
        }
    }

    public static <T> List<T> genListByCopyProperties(List list, Class<T> clazz) {
        List<T> result = Lists.newArrayList();
        if (list == null) {
            return result;
        } else {
            try {
                Iterator var3 = list.iterator();

                while(var3.hasNext()) {
                    Object source = var3.next();
                    T t = clazz.newInstance();
                    BeanUtils.copyProperties(source, t);
                    result.add(t);
                }

                return result;
            } catch (Exception var6) {
                LOG.error("genVo报错", var6);
                return null;
            }
        }
    }

    public static <T> Page<T> genPageByCopyProperties(Page pageInfo, Class<T> clazz) {
        if (pageInfo == null) {
            return null;
        } else {
            ArrayList result = Lists.newArrayList();

            try {
                Iterator var3 = pageInfo.getRecords().iterator();

                while(var3.hasNext()) {
                    Object source = var3.next();
                    T t = clazz.newInstance();
                    BeanUtils.copyProperties(source, t);
                    result.add(t);
                }
            } catch (Exception var6) {
                LOG.error("genVo报错", var6);
                return null;
            }

            Page<T> pageInfo1 = new Page<>();
            pageInfo1.setRecords(result);
            pageInfo1.setCurrent(pageInfo.getCurrent());
            pageInfo1.setSize(pageInfo.getSize());
            pageInfo1.setTotal(pageInfo.getTotal());
            return pageInfo1;
        }
    }

    public static String set2String(Set<String> set, String split) {
        if (set == null) {
            return "";
        } else {
            List<String> list = new ArrayList(set);
            return list2String(list, split);
        }
    }

    public static String list2String(List list, String split) {
        if (list == null) {
            return "";
        } else {
            String result = "";

            for(int i = 0; i < list.size(); ++i) {
                if (!result.equals("")) {
                    result = result + split;
                }

                result = result + String.valueOf(list.get(i));
            }

            return result;
        }
    }
}

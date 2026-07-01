package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.MonthlyRevenue;
import com.training.management.domain.entity.TodoItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DashboardMapper {

    @Select("SELECT id, month_label, amount FROM monthly_revenue ORDER BY id")
    List<MonthlyRevenue> findMonthlyRevenue();

    @Select("SELECT id, title, deadline, level, completed FROM todo_item WHERE completed = 0 ORDER BY id")
    List<TodoItem> findTodoItems();
}

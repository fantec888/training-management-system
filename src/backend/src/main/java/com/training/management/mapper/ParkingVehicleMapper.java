package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.ParkingVehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ParkingVehicleMapper {

    @Select("""
        SELECT id, plate, owner, vehicle_type, location, status, monthly_fee
        FROM parking_vehicle
        ORDER BY id
        """)
    List<ParkingVehicle> findAll();

    @Select("SELECT COUNT(*) FROM parking_vehicle WHERE vehicle_type = '固定车'")
    long countFixed();

    @Select("SELECT COUNT(*) FROM parking_vehicle WHERE vehicle_type = '临停车'")
    long countTemporary();
}

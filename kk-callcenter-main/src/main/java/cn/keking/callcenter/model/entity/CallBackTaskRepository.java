package cn.keking.callcenter.model.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @auther: chenjh
 * @time: 2018/11/28 15:41
 * @description
 */
@Repository
public interface CallBackTaskRepository extends JpaRepository<CallBackTaskPO, String> {
}

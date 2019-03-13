package cn.keking.callcenter.model.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @auther: chenjh
 * @time: 2019/3/4 9:39
 * @description
 */
public interface WebHookRepository extends JpaRepository<WebHookPO, Integer> {
}

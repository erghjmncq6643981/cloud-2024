/*
 * chandler-jpa
 * 2024/12/25 15:07
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.jpa.jdk8.example.domain.repository;

import com.chandler.jpa.jdk8.example.domain.dataobject.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/12/25 15:07
 * @version 1.0.0
 * @since 1.8
 */
public interface BookRepository extends JpaRepository<Book, Long> {
}
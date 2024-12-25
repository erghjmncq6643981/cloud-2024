/*
 * chandler-jpa
 * 2024/12/25 15:08
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.jpa.example.service;

import com.chandler.jpa.example.domain.dataobject.Book;
import com.chandler.jpa.example.domain.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/12/25 15:08
 * @version 1.0.0
 * @since 1.8
 */
@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    //查询所有图书
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    //查询单个图书
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    //保存单个图书
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book update(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        return bookRepository.save(book);
    }

    //删除单个图书
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
create database library;
use library;

insert into library.books (id, author, quantity, title)
values  (1, 'Nguyễn Nhật Ánh', 10, 'Tôi thấy hoa vàng trên cỏ xanh'),
        (2, 'Giản Tư Trung', 15, 'Đúng việc');

select * from books;
package com.example.books;

import java.util.Comparator;

public class SortClass {

    protected static class BookValues {
        String name, author,category,publishDate,pageCount;

        public BookValues(String name,String author, String category,String publish_date,String page_count) {
            this.name = name;
            this.author = author;
            this.category = category;
            this.publishDate = publish_date;
            this.pageCount = page_count;
        }

        public String getName() {
            return name;
        }

        public String getAuthor() {
            return author;
        }

        public String getCategory() {
            return category;
        }

        public String getPublishDate() {
            return publishDate;
        }

        public String getPageCount() {
            return pageCount;
        }
    }

    protected static class SortbyName implements Comparator<BookValues>
    {
        public int compare(BookValues a, BookValues b)
        {
            return a.name.compareTo(b.name);
        }
    }

    protected static class SortbyAuthor implements Comparator<BookValues>
    {
        public int compare(BookValues a, BookValues b)
        {
            return a.author.compareTo(b.author);
        }
    }

    protected static class SortbyCategory implements Comparator<BookValues>
    {
        public int compare(BookValues a, BookValues b)
        {
            return a.category.compareTo(b.category);
        }
    }

    protected static class SortbyDate implements Comparator<BookValues>
    {
        public int compare(BookValues a, BookValues b)
        {
            String aYear = "", bYear = "";
            String aDate = a.getPublishDate();
            String bDate = b.getPublishDate();

            for(int i = aDate.length()-4; i < aDate.length(); i++) {
                aYear += aDate.charAt(i);
            }

            for(int i = bDate.length()-4; i < bDate.length(); i++) {
                bYear += bDate.charAt(i);
            }

            return aYear.compareTo(bYear);
        }
    }

    protected static class SortbyPageCount implements Comparator<BookValues>
    {
        public int compare(BookValues a, BookValues b)
        {
            return a.pageCount.compareTo(b.pageCount);
        }
    }

}

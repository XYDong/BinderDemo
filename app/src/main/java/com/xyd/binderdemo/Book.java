package com.xyd.binderdemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ProjectName: BinderDemo
 * @Package: com.xyd.binderdemo
 * @ClassName: Book
 * @Description: java类作用描述
 * @Author: 作者名
 * @CreateDate: 2019-06-26 21:37
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019-06-26 21:37
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class Book implements Parcelable {
    public int id;
    public String name;
    public String author;

    public Book() {
    }

    public Book(int id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }

    protected Book(Parcel in) {
        id = in.readInt();
        name = in.readString();
        author = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(author);
    }
}

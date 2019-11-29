package com.dazhongmianfei.dzmfreader.localapp;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.localapp.bean.Book;




import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by will on 2016/10/29.
 */

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {
    private List<Book> books;
    private Context mContext;
    private ClickCallback mCallback;

    private boolean isDeleteButtonVisible;

    public BookListAdapter(Context context,List<Book> books) {
        this.books = books;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = books.get(position);
        String bookName = book.getBookName().substring(0, book.getBookName().lastIndexOf("."));
        holder.title.setText(bookName);
        // holder.preview.setText(getPreview(book));
        //int progress = getProgress(book);
        //int progress = (int)(new Random().nextFloat()*100);
        // holder.root.setPercentage(progress);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.local_main_item, parent, false);
        return new BookViewHolder(view);
    }

    public boolean isDeleteButtonVisible() {
        return isDeleteButtonVisible;
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title;//,preview;

        //CustomLayout root;
        BookViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.main_book_item_title);
            view.findViewById(R.id.main_book_item_title_lay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallback != null) {
                        mCallback.onClick(books.get(getLayoutPosition()));
                    }
                }
            });
            view.findViewById(R.id.main_book_item_title_lay).setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    if (mCallback != null) {
                        mCallback.onLongClick(books.get(getLayoutPosition()));
                    }

                    return true;
                }
            });
            // preview = view.findViewById(R.id.main_book_item_preview);
            // root = view.findViewById(R.id.main_book_item_root);


          /*  view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isDeleteButtonVisible()){
                        showDeleteButton(false);
                        return;
                    }
                    if(mCallback != null){
                        mCallback.onClick(books.get(getLayoutPosition()));
                    }
                }
            });
            root.setOnDeleteButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   deleteBook(getLayoutPosition());
                }
            });
            mChildren.add(root);*/
        }

    }
/*    public void showDeleteButton(boolean which){
        isDeleteButtonVisible = which;
        for (CustomLayout customLayout : mChildren){
            if(which){
                customLayout.showDeleteButton();
            }else{
                customLayout.hideDeleteButton();
            }

        }
    }*/

    private List<Book> getTestData() {
        List<Book> list = new ArrayList<>();
        Book book;
        for (int i = 0; i < 50; i++) {
            book = new Book();
            book.setBookName("" + i);
            list.add(book);
        }
        return list;
    }


    //将选中的文件添加到列表及数据库中，因为存在对比操作，可能比较耗时，故这里新开一个线程执行并展示ProgressDialog
    public void addBookFromFile(final Context context, final List<File> files) {
        final List<Book> newDataList = new ArrayList<>();
      //  final ProgressDialog dialog = Util.createProgressDialog(context, "处理中");
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("处理中");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (File file : files) {
                    if (!getBooks().contains(new Book(file.getName(), file.getPath()))) {
                        newDataList.add(new Book(file.getName(), file.getPath()));
                    }
                }
                if (newDataList.size() == 0) {
                    dialog.cancel();
                    return;
                }
                ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        books.addAll(newDataList);
                        notifyDataSetChanged();

                        LitePal.saveAllAsync(newDataList);
                    }
                });
                dialog.cancel();
            }
        }).start();
    }



    public void deleteBook(Book book) {
        books.remove(book);
        notifyDataSetChanged();
    }


    public List<Book> getBooks() {
        return books;
    }

    public void clearData() {
        books.clear();
        notifyDataSetChanged();
    }



    public void setOnClickCallback(ClickCallback callback) {
        mCallback = callback;
    }

    public interface ClickCallback {
        void onClick(Book book);

        void onLongClick(Book book);
    }



}

package com.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Item;
import com.example.form.ItemForm;
import com.example.repository.ItemRepository;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    
    public List<Item> findAll() {
        return this.itemRepository.findAll();
    }
    
 // データ保存用のメソッド
    public Item save(ItemForm itemForm) {
        // Entityクラスのインスタンスを生成
        Item item = new Item();
        // フィールドのセットを行う
        item.setName(itemForm.getName());
        item.setPrice(itemForm.getPrice());
        item.setCategoryId(itemForm.getCategoryId());
     // 新規登録時は在庫数に0をセットする
        item.setStock(0);
        // repository.saveメソッドを利用してデータの保存を行う
        return this.itemRepository.save(item);
    }
    
 // 選択されたIDに関する情報を取得して返す
    public Item findById(Integer id) {
      Optional<Item> optionalItem = this.itemRepository.findById(id); // Optional型→nullではない場合（値がある場合に実行される（if的な））
      Item item  = optionalItem.get(); // Optionl型に値が入っている場合、その値を返す
      return item; // つまり 選択されたidの情報？を返す
    }
    
 // データ更新用のメソッド
    public Item update(Integer id, ItemForm itemForm) {
        // データ１件分のEntityクラスを取得　（前に登録した情報を持ってくるよ～？）
        Item item = this.findById(id);
        // Formクラスのフィールドをセット　（その前の情報を上書きするよ～？）
        item.setName(itemForm.getName());
        item.setPrice(itemForm.getPrice());
        // repository.saveメソッドを利用して データの保存
        item.setCategoryId(itemForm.getCategoryId());
        return this.itemRepository.save(item);
    }
    
 // データ削除用のメソッド
    public Item delete(Integer id) {
        // this.itemRepository.deleteById(id); (←物理削除)
     // idから該当のEntityクラスを取得します
        Item item = this.findById(id);
        // EntityクラスのdeletedAtフィールドを現在日時で上書きします
        item.setDeletedAt(LocalDateTime.now());
        // 更新処理
        return this.itemRepository.save(item);
    }
    
 // メソッドの削除メソッドl
    public List<Item> findByDeletedAtIsNull() {
      return this.itemRepository.findByDeletedAtIsNull();
  }
    
 // 入荷処理
    public Item nyuka(Integer id, Integer inputValue) {
        Item item = this.findById(id);
        // 商品の在庫数に対して入力値分加算する
        item.setStock(item.getStock() + inputValue);
        // 在庫数の変動を保存
        return this.itemRepository.save(item);
    }

 // 出荷処理
    public Item shukka(Integer id, Integer inputValue) {
        Item item = this.findById(id);
        // 入力値が在庫数以内かを判定する
        if (inputValue <= item.getStock()) {
            // 在庫数から入力値を引いた値をセットする
            item.setStock(item.getStock() - inputValue);
        }

        // 在庫数の変動を保存
        return this.itemRepository.save(item);
    }
 
}
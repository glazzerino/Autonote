package com.fbu.autonote.utilities;

import com.fbu.autonote.models.CustomNode;

public class LinkedListCustom<T> {
    /**
     * @class simple implementation of a doubly linked list that returns references to node when adding items
     */
    private CustomNode<T> front;
    private CustomNode<T> back;
    public static final String TAG = "LinkedListCustom";
    int size;

    public LinkedListCustom() {
        front = null;
        back = null;
        size = 0;
    }

    public CustomNode<T> add(T data) {
        CustomNode node = new CustomNode(data);
        if (front == null) {
            this.front = node;
        } else {
            back.next = node;
            node.prev = this.back;
        }
        back = node;
        size++;
        return this.back;
    }

    public void deleteFront() {
        front = this.front.next;
        if (front != null) {
            front.prev = null;
        }
    }

    public void deleteBack() {
        back = this.back.prev;
        if (back != null) {
            back.next = null;
        }
    }

    public void deleteNode(CustomNode<T> node) {
        if (node == back && node == front) {
            front = back = null;
        }
        else if (node == front) {
            deleteFront();
        } else if (node == back) {
            deleteBack();
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            node.prev = null;
            node.next = null;
        }
        size--;
    }

    //Assumes node is present in list
    public void moveToBack(CustomNode<T> node) throws NullPointerException {
        if (node != back && node != front) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            back.next = node;
            node.prev = back;
            node.next = null;
        } else if (node == front) {
            back.next = node;
            node.prev = back;
            front = node.next;
            front.prev = null;
            node.next = null;
        }
        this.back = node;
    }

    public CustomNode<T> getBack() {
        return this.back;
    }

    public CustomNode<T> getFront() {
        return this.front;
    }
}

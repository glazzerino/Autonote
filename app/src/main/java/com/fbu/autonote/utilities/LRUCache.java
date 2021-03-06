package com.fbu.autonote.utilities;

import com.fbu.autonote.models.CustomNode;

import java.util.HashMap;
import java.util.Iterator;

public class LRUCache<T> implements Iterator<T> {
    /**
     * @class This is a basic implementation of the LRU Cache Policy, which discards the item at the front of a queue
     * when the number of items exceeds a specified threshold
     * @field limit non-inclusive limit of items allowed to exist inside the container
     * @field pointerTable stores a pointer to the item inside the queue for fast verification of existence
     * @field next used as part of the implementation of the Iterator<T> interface
     */
    public LinkedListCustom<T> container;
    private final HashMap<T, CustomNode<T>> pointerTable;
    CustomNode<T> current; //For iteration
    int limit;

    public LRUCache(int limit) {
        this.limit = limit;
        container = new LinkedListCustom<>();
        pointerTable = new HashMap<>(limit, 0.8f);
        current = null;
    }

    public void delete(T data) {
        CustomNode<T> node = pointerTable.get(data);
        container.deleteNode(node);
        pointerTable.remove(data);
        current = container.getBack();
    }

    public CustomNode<T> getNode(T data) {
        return pointerTable.get(data);
    }

    public void update(T data) {
        if (pointerTable.containsKey(data)) {
            container.moveToBack(pointerTable.get(data));
            pointerTable.replace(data, container.getBack());
        } else {
            container.add(data);
            pointerTable.put(data, container.getBack());
            //Maintain queue size
            if (container.size > limit) {
                pointerTable.remove(container.getFront().data);
                container.deleteFront();
            }
        }
        this.current = container.getBack();
    }

    @Override
    public boolean hasNext() {
        if (current != null) {
            return true;
        }
        current = container.getBack();
        return false;
    }

    @Override
    public T next() {
        T data = current.data;
        current = current.prev;
        return data;
    }

    public int size() {
        return container.size;
    }
}

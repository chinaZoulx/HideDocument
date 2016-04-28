package com.chris.hidedocument.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.lang.reflect.Constructor;

/**
 * Created by zoulx on 2015/8/18.
 */
public class TabInfoBean implements Parcelable {
    private int id;
    private int icon;
    private String name = null;
    public boolean hasTips = false;
    public Fragment fragment = null;
    public boolean notifyChange = false;
    @SuppressWarnings("rawtypes")
    public Class fragmentClass = null;

    @SuppressWarnings("rawtypes")
    public TabInfoBean(int id, String name, Class clazz) {
        this(id, name, 0, clazz);
    }

    @SuppressWarnings("rawtypes")
    public TabInfoBean(int id, String name, boolean hasTips, Class clazz) {
        this(id, name, 0, clazz);
        this.hasTips = hasTips;
    }

    @SuppressWarnings("rawtypes")
    public TabInfoBean(int id, String name, int iconid, Class clazz) {
        super();

        this.name = name;
        this.id = id;
        icon = iconid;
        fragmentClass = clazz;
    }

    public TabInfoBean(Parcel p) {
        this.id = p.readInt();
        this.name = p.readString();
        this.icon = p.readInt();
        this.notifyChange = p.readInt() == 1;
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

    public void setIcon(int iconid) {
        icon = iconid;
    }

    public int getIcon() {
        return icon;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Fragment createFragment() {
        if (fragment == null) {
            Constructor constructor;
            try {
                constructor = fragmentClass.getConstructor(new Class[0]);
                fragment = (Fragment) constructor.newInstance(new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fragment;
    }

    public static final Creator<TabInfoBean> CREATOR = new Creator<TabInfoBean>() {
        public TabInfoBean createFromParcel(Parcel p) {
            return new TabInfoBean(p);
        }

        public TabInfoBean[] newArray(int size) {
            return new TabInfoBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel p, int flags) {
        p.writeInt(id);
        p.writeString(name);
        p.writeInt(icon);
        p.writeInt(notifyChange ? 1 : 0);
    }
}

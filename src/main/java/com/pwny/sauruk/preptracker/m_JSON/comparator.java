package com.pwny.sauruk.preptracker.m_JSON;

import java.util.Comparator;

public class comparator implements Comparator<Label> {
    @Override
    public int compare(Label a, Label b) {
        return a.expiresAt < b.expiresAt ? -1 : a.expiresAt == b.expiresAt ? 0 : 1;
    }
}

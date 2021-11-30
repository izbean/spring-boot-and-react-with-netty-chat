package com.izbean.springbootnettychat.config.socket.provider;

import java.util.*;

public class NicknameProvider {

    private final Queue<String> pool;
    private final Set<String> preset;
    private final Set<String> occupied = new HashSet<>();

    public NicknameProvider() {
        List<String> names = Arrays.asList(
                "Muzi", "Ryan", "Con", "Apeach", "Jay-G", "Frodo", "Neo", "Tube"
        );

        preset = new HashSet<>(names);
        Collections.shuffle(names);
        pool = new LinkedList<>(names);
    }

    public synchronized boolean available(String nickname) {
        return !preset.contains(nickname) && !occupied.contains(nickname);
    }

    public synchronized String reserve() {
        String n = pool.poll();
        if (n != null) occupied.add(n);
        return n;
    }

    public synchronized void reserve(String custom) {
        if (!available(custom)) throw new RuntimeException("not available name");
        occupied.add(custom);
    }

    public synchronized NicknameProvider release(String nick) {
        occupied.remove(nick);
        if (preset.contains(nick) && !pool.contains(nick)) pool.add(nick);
        return this;
    }

}

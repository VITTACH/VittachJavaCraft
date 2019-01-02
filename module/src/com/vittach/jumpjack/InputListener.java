package com.vittach.jumpjack;

import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;

public class InputListener implements InputProcessor, ProcessorInput {
    private boolean all_proc;
    private int numberOf_ID = 0;
    private ArrayList<ProcessorInput>
            proces = new ArrayList<ProcessorInput>();

    void clnProces() {
        proces.clear();
    }

    @Override
    public boolean scrolled(int mont) {
        return true;
    }

    void addListener(ProcessorInput p) {
        p.setIDOffset(numberOf_ID++);
        proces.add(p);
    }

    void delProces(ProcessorInput p) {
        proces.remove(p);
    }

    public void setIDOffset(int iset) {
        numberOf_ID = iset;
    }

    @Override
    public boolean keyUp(int id_key) {
        all_proc = true;
        for (ProcessorInput Procs : proces)
            all_proc = all_proc && Procs.keyUp(id_key);
        return all_proc;
    }

    @Override
    public boolean keyDown(int i_key) {
        all_proc = true;
        for (ProcessorInput Proces : proces)
            all_proc = all_proc && Proces.keyDown(i_key);
        return all_proc;
    }

    @Override
    public boolean keyTyped(char chr) {
        all_proc = true;
        for (ProcessorInput Proces : proces)
            all_proc = all_proc && Proces.keyTyped(chr);
        return all_proc;
    }

    @Override
    public boolean
    mouseMoved(int screnx, int screny) {
        all_proc = true;
        for (ProcessorInput Proces : proces)
            all_proc = all_proc && Proces.mouseMoved(screnx, screny);
        return all_proc;
    }

    @Override
    public boolean
    touchDragged(int x, int y, int numb_ID) {
        all_proc = true;
        for (ProcessorInput Proces : proces)
            all_proc = all_proc && Proces.touchDragged(x, y, numb_ID);
        return all_proc;
    }

    @Override
    public boolean
    touchUp(int x, int y, int pointer, int id) {
        all_proc = true;
        for (ProcessorInput Proces : proces)
            all_proc = all_proc && Proces.touchUp(x, y, pointer, id);
        return all_proc;
    }

    @Override
    public boolean
    touchDown(int x, int y, int ipointer, int p) {
        all_proc = true;
        for (ProcessorInput proc : proces)
            all_proc = all_proc && proc.touchDown(x, y, ipointer, p);
        return all_proc;
    }
}
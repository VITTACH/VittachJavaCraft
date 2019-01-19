package com.vittach.jumpjack;

import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;

public class InputListener implements InputProcessor, ProcessorInput {
    private boolean allProcess;
    private int idCounter = 0;
    private ArrayList<ProcessorInput> processes = new ArrayList<ProcessorInput>();

    public void cleanProcesses() {
        processes.clear();
    }

    @Override
    public boolean scrolled(int amount) {
        return true;
    }

    public void addListener(ProcessorInput processorInput) {
        processorInput.setIDOffset(idCounter++);
        processes.add(processorInput);
    }

    public void delProces(ProcessorInput processorInput) {
        processes.remove(processorInput);
    }

    public void setIDOffset(int offset) {
        idCounter = offset;
    }

    @Override
    public boolean keyUp(int idKey) {
        allProcess = true;
        for (ProcessorInput process : processes) {
            allProcess = allProcess && process.keyUp(idKey);
        }
        return allProcess;
    }

    @Override
    public boolean keyDown(int idkey) {
        allProcess = true;
        for (ProcessorInput process : processes) {
            allProcess = allProcess && process.keyDown(idkey);
        }
        return allProcess;
    }

    @Override
    public boolean keyTyped(char code) {
        allProcess = true;
        for (ProcessorInput process : processes) {
            allProcess = allProcess && process.keyTyped(code);
        }
        return allProcess;
    }

    @Override
    public boolean mouseMoved(int screenX, int screnY) {
        allProcess = true;
        for (ProcessorInput process : processes) {
            allProcess = allProcess && process.mouseMoved(screenX, screnY);
        }
        return allProcess;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        allProcess = true;
        for (ProcessorInput process : processes) {
            allProcess = allProcess && process.touchDragged(x, y, pointer);
        }
        return allProcess;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int pid) {
        allProcess = true;
        for (ProcessorInput process : processes) {
            allProcess = allProcess && process.touchUp(x, y, pointer, pid);
        }
        return allProcess;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int p) {
        allProcess = true;
        for (ProcessorInput process : processes) {
            allProcess = allProcess && process.touchDown(x, y, pointer, p);
        }
        return allProcess;
    }
}
package com.vittach.jumpjack.ui;

import com.badlogic.gdx.InputProcessor;
import com.vittach.jumpjack.engine.controller.ProcessorInput;

import java.util.ArrayList;

public class InputListener implements InputProcessor, com.vittach.jumpjack.engine.controller.ProcessorInput {
    private boolean allProcess;
    private int idCounter = 0;
    private ArrayList<com.vittach.jumpjack.engine.controller.ProcessorInput> processes = new ArrayList<com.vittach.jumpjack.engine.controller.ProcessorInput>();

    public void cleanProcesses() {
        processes.clear();
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return true;
    }

    public void addListener(com.vittach.jumpjack.engine.controller.ProcessorInput processorInput) {
        processorInput.setIDOffset(idCounter++);
        processes.add(processorInput);
    }

    public void delProces(com.vittach.jumpjack.engine.controller.ProcessorInput processorInput) {
        processes.remove(processorInput);
    }

    public void setIDOffset(int offset) {
        idCounter = offset;
    }

    @Override
    public boolean keyUp(int idKey) {
        allProcess = true;
        for (com.vittach.jumpjack.engine.controller.ProcessorInput process : processes) {
            allProcess = allProcess && process.keyUp(idKey);
        }
        return allProcess;
    }

    @Override
    public boolean keyDown(int idkey) {
        allProcess = true;
        for (com.vittach.jumpjack.engine.controller.ProcessorInput process : processes) {
            allProcess = allProcess && process.keyDown(idkey);
        }
        return allProcess;
    }

    @Override
    public boolean keyTyped(char code) {
        allProcess = true;
        for (com.vittach.jumpjack.engine.controller.ProcessorInput process : processes) {
            allProcess = allProcess && process.keyTyped(code);
        }
        return allProcess;
    }

    @Override
    public boolean mouseMoved(int screenX, int screnY) {
        allProcess = true;
        for (com.vittach.jumpjack.engine.controller.ProcessorInput process : processes) {
            allProcess = allProcess && process.mouseMoved(screenX, screnY);
        }
        return allProcess;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        allProcess = true;
        for (com.vittach.jumpjack.engine.controller.ProcessorInput process : processes) {
            allProcess = allProcess && process.touchDragged(x, y, pointer);
        }
        return allProcess;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int pid) {
        allProcess = true;
        for (com.vittach.jumpjack.engine.controller.ProcessorInput process : processes) {
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
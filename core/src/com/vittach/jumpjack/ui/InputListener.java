package com.vittach.jumpjack.ui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.vittach.jumpjack.engine.controller.ProcessorInput;

import java.util.ArrayList;

public class InputListener extends Stage implements InputProcessor, ProcessorInput {
    private boolean allProcess;
    private int idOffset = 0;
    private ArrayList<ProcessorInput> processes = new ArrayList<ProcessorInput>();

    public void cleanProcesses() {
        processes.clear();
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return true;
    }

    public void addListener(ProcessorInput processorInput) {
        processorInput.setIdOffset(idOffset += 10);
        processes.add(processorInput);
    }

    public void delProcess(ProcessorInput processorInput) {
        processes.remove(processorInput);
    }

    public void setIdOffset(int idOffset) {
        this.idOffset = idOffset;
    }

    @Override
    public boolean keyUp(int keyCode) {
        super.keyUp(keyCode);
        allProcess = true;
        for (ProcessorInput process : processes) {
            allProcess = allProcess && process.keyUp(keyCode);
        }
        return allProcess;
    }

    @Override
    public boolean keyDown(int keyCode) {
        super.keyDown(keyCode);
        allProcess = true;
        for (ProcessorInput process : processes) {
            allProcess = allProcess && process.keyDown(keyCode);
        }
        return allProcess;
    }

    @Override
    public boolean keyTyped(char keyCode) {
        super.keyTyped(keyCode);
        allProcess = true;
        for (ProcessorInput process : processes) {
            allProcess = allProcess && process.keyTyped(keyCode);
        }
        return allProcess;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        super.mouseMoved(x, y);
        allProcess = true;
        for (ProcessorInput process : processes) {
            allProcess = allProcess && process.mouseMoved(x, y);
        }
        return allProcess;
    }

    @Override
    public boolean touchDragged(int x, int y, int id) {
        super.touchDragged(x, y, id);
        allProcess = true;
        for (ProcessorInput process : processes) {
            allProcess = allProcess && process.touchDragged(x, y, id);
        }
        return allProcess;
    }

    @Override
    public boolean touchUp(int x, int y, int id, int button) {
        super.touchUp(x, y, id, button);
        allProcess = true;
        for (ProcessorInput process : processes) {
            allProcess = allProcess && process.touchUp(x, y, id, button);
        }
        return allProcess;
    }

    @Override
    public boolean touchDown(int x, int y, int id, int button) {
        super.touchDown(x, y, id, button);
        allProcess = true;
        for (ProcessorInput process : processes) {
            allProcess = allProcess && process.touchDown(x, y, id, button);
        }
        return allProcess;
    }
}
package com.vittach.jumpjack;

import com.vittach.jumpjack.framework.ImageHandler;

import java.util.Scanner;

public class percepton {
    ImageHandler image;
    int width, hght;
    int input[][];
    int weight[][];
    Scanner scaner;
    int limit = 9, i, j, sum = 0;

    percepton() {
        image = new ImageHandler();
        scaner = new Scanner
                (System.in);
    }

    void init() {
        load();
        width = 3;
        hght = 5;
        input = new int[width][hght];
        weight = new int[width][hght];
        for (i = 0; i < width; i++)
            for (j = 0; j < hght; j++)
                weight[i][j] = 0;
    }

    void load() {
        System.out.println("PATH to");
        image.load(scaner.nextLine());
    }

    void check() {
        if (sum >= limit) {
            System.out.println("ITs TRUE");
            System.out.println("correct?");
            System.out.println("Y=yes/No");
            if (scaner.nextLine().equals("N"))
                for (i = 0; i < width; i++)
                    for (j = 0; j < hght; j++)
                        weight[i][j] -= input[i][j];
        } else {
            System.out.println("IT FALSE");
            System.out.println("correct?");
            System.out.println("Y=yes/No");
            if (scaner.nextLine().equals("N"))
                for (i = 0; i < width; i++)
                    for (j = 0; j < hght; j++)
                        weight[i][j] += input[i][j];
        }
    }

    void print() {
        for (j = 0; j < hght; j++) {
            for (i = 0; i < width; i++)
                System.out.print(weight[i][j]);
            System.out.println();
        }
    }

    void study() {
        sum = 0;
        for (i = 0; i < width; i++)
            for (j = 0; j < hght; j++)
                if (image.get_Pixel(i, j) >= 250)
                    input[i][j] = 0;
                else
                    input[i][j] = 1;
        for (i = 0; i < width; i++)
            for (j = 0; j < hght; j++)
                sum += input[i][j] * weight[i][j];
        check();
        print();
        load();
    }
}

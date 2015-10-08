package cn.ddshcool.entity;

/**
 * 用于判断当前用户等级
 * Created by yosemite on 15/9/10.
 */
public class Level {

    public static int whatLevel(Integer experience){
        int level = 0;

        if(experience <= 50){
            level = 0;
        }else if(experience <= 100){
            level = 1;
        }else if(experience <= 240){
            level = 2;
        }else if(experience <= 380){
            level = 3;
        }else if(experience <= 580){
            level = 4;
        }else if(experience <= 830){
            level = 5;
        }

        return level;
    }


}

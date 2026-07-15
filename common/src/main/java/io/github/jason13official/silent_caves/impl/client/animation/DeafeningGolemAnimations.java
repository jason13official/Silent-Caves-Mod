package io.github.jason13official.silent_caves.impl.client.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationChannel.Interpolations;
import net.minecraft.client.animation.AnimationChannel.Targets;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.AnimationDefinition.Builder;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class DeafeningGolemAnimations {

  public static final AnimationDefinition WALK = Builder.withLength(1.807F).looping()
      .addAnimation("head", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), Interpolations.CATMULLROM),
          new Keyframe(0.5313F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.9564F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), Interpolations.CATMULLROM),
          new Keyframe(1.3815F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(1.8066F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armLeft", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.6376F, KeyframeAnimations.degreeVec(-34.73F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(1.0627F, KeyframeAnimations.degreeVec(-11.56F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(1.8066F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armLeft", new AnimationChannel(Targets.POSITION,
          new Keyframe(0.0F, KeyframeAnimations.posVec(-0.5F, -1.0F, 1.5F), Interpolations.CATMULLROM),
          new Keyframe(0.2125F, KeyframeAnimations.posVec(-0.21F, 8.58F, 2.06F), Interpolations.CATMULLROM),
          new Keyframe(0.6376F, KeyframeAnimations.posVec(-0.06F, 0.01F, -3.47F), Interpolations.CATMULLROM),
          new Keyframe(1.0627F, KeyframeAnimations.posVec(0.0F, 2.0F, -3.0F), Interpolations.CATMULLROM),
          new Keyframe(1.4878F, KeyframeAnimations.posVec(-0.31F, 1.36F, -1.0F), Interpolations.CATMULLROM),
          new Keyframe(1.8066F, KeyframeAnimations.posVec(-0.5F, -1.0F, 1.5F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armRight", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(-34.73F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.3188F, KeyframeAnimations.degreeVec(-11.56F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(1.0627F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(1.8066F, KeyframeAnimations.degreeVec(-34.73F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armRight", new AnimationChannel(Targets.POSITION,
          new Keyframe(0.0F, KeyframeAnimations.posVec(-0.06F, 0.01F, -3.47F), Interpolations.CATMULLROM),
          new Keyframe(0.3188F, KeyframeAnimations.posVec(0.0F, 2.0F, -3.0F), Interpolations.CATMULLROM),
          new Keyframe(0.7439F, KeyframeAnimations.posVec(-0.31F, 1.36F, -1.0F), Interpolations.CATMULLROM),
          new Keyframe(1.0627F, KeyframeAnimations.posVec(-0.5F, -1.0F, 1.5F), Interpolations.CATMULLROM),
          new Keyframe(1.2752F, KeyframeAnimations.posVec(-0.21F, 8.58F, 2.06F), Interpolations.CATMULLROM),
          new Keyframe(1.8066F, KeyframeAnimations.posVec(-0.06F, 0.01F, -3.47F), Interpolations.CATMULLROM)
      ))
      .build();

  public static final AnimationDefinition SPRINT = Builder.withLength(0.8157F).looping()
      .addAnimation("body", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.3968F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.8157F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("head", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
          new Keyframe(0.2425F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.6614F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), Interpolations.CATMULLROM),
          new Keyframe(0.8157F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armLeft", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.3307F, KeyframeAnimations.degreeVec(-34.73F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.5512F, KeyframeAnimations.degreeVec(-11.56F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.8157F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armLeft", new AnimationChannel(Targets.POSITION,
          new Keyframe(0.0F, KeyframeAnimations.posVec(-0.5F, -1.0F, 1.5F), Interpolations.CATMULLROM),
          new Keyframe(0.1102F, KeyframeAnimations.posVec(-0.21F, 8.58F, 2.06F), Interpolations.CATMULLROM),
          new Keyframe(0.3307F, KeyframeAnimations.posVec(-0.06F, 0.01F, -3.47F), Interpolations.CATMULLROM),
          new Keyframe(0.5512F, KeyframeAnimations.posVec(0.0F, 2.0F, -3.0F), Interpolations.CATMULLROM),
          new Keyframe(0.6614F, KeyframeAnimations.posVec(-0.31F, 1.36F, -1.0F), Interpolations.CATMULLROM),
          new Keyframe(0.8157F, KeyframeAnimations.posVec(-0.5F, -1.0F, 1.5F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armRight", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.2425F, KeyframeAnimations.degreeVec(-34.73F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.485F, KeyframeAnimations.degreeVec(-11.56F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.8157F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armRight", new AnimationChannel(Targets.POSITION,
          new Keyframe(0.0F, KeyframeAnimations.posVec(-0.5F, -1.0F, 1.5F), Interpolations.CATMULLROM),
          new Keyframe(0.1102F, KeyframeAnimations.posVec(-0.21F, 8.58F, 2.06F), Interpolations.CATMULLROM),
          new Keyframe(0.2425F, KeyframeAnimations.posVec(-0.06F, 0.01F, -3.47F), Interpolations.CATMULLROM),
          new Keyframe(0.485F, KeyframeAnimations.posVec(0.0F, 2.0F, -3.0F), Interpolations.CATMULLROM),
          new Keyframe(0.6614F, KeyframeAnimations.posVec(-0.31F, 1.36F, -1.0F), Interpolations.CATMULLROM),
          new Keyframe(0.8157F, KeyframeAnimations.posVec(-0.5F, -1.0F, 1.5F), Interpolations.CATMULLROM)
      ))
      .build();

  public static final AnimationDefinition ATTACK_FLING = Builder.withLength(1.1667F)
      .addAnimation("head", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.125F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armLeft", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.25F, KeyframeAnimations.degreeVec(-202.5F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armLeft", new AnimationChannel(Targets.POSITION,
          new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
          new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -4.0F, -7.0F), Interpolations.LINEAR),
          new Keyframe(0.7917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
      ))
      .addAnimation("armRight", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.125F, KeyframeAnimations.degreeVec(-202.5F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armRight", new AnimationChannel(Targets.POSITION,
          new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
          new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, -5.0F, -7.0F), Interpolations.LINEAR),
          new Keyframe(0.7917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
      ))
      .build();

  public static final AnimationDefinition ATTACK_SWIPE_RIGHT = Builder.withLength(1.3333F)
      .addAnimation("body", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, -42.5F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("head", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -15.0F), Interpolations.CATMULLROM),
          new Keyframe(0.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("head", new AnimationChannel(Targets.POSITION,
          new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.2917F, KeyframeAnimations.posVec(6.0F, 0.0F, 1.0F), Interpolations.CATMULLROM),
          new Keyframe(1.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armRight", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
          new Keyframe(0.125F, KeyframeAnimations.degreeVec(-72.5F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-21.8904F, -33.4341F, -23.3725F), Interpolations.CATMULLROM),
          new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .build();

  public static final AnimationDefinition CRAWL = Builder.withLength(1.2083F).looping()
      .addAnimation("body", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(55.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("body", new AnimationChannel(Targets.POSITION,
          new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -11.2F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("head", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), Interpolations.CATMULLROM),
          new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), Interpolations.CATMULLROM),
          new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), Interpolations.CATMULLROM),
          new Keyframe(0.9167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), Interpolations.CATMULLROM),
          new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), Interpolations.CATMULLROM)
      ))
      .addAnimation("head", new AnimationChannel(Targets.POSITION,
          new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -32.9F, -22.4F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armLeft", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), Interpolations.LINEAR),
          new Keyframe(0.4583F, KeyframeAnimations.degreeVec(46.3147F, -14.7524F, -57.01F), Interpolations.CATMULLROM),
          new Keyframe(0.8333F, KeyframeAnimations.degreeVec(34.8965F, -3.052F, -42.1414F), Interpolations.CATMULLROM),
          new Keyframe(1.2083F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armLeft", new AnimationChannel(Targets.POSITION,
          new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 1.4F, -5.7F), Interpolations.LINEAR),
          new Keyframe(0.4583F, KeyframeAnimations.posVec(-2.0F, 1.4F, -5.7F), Interpolations.LINEAR),
          new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 1.4F, -5.7F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armRight", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), Interpolations.LINEAR),
          new Keyframe(0.4583F, KeyframeAnimations.degreeVec(46.3147F, -14.7524F, 57.01F), Interpolations.CATMULLROM),
          new Keyframe(0.8333F, KeyframeAnimations.degreeVec(34.8965F, -3.052F, 42.1414F), Interpolations.CATMULLROM),
          new Keyframe(1.2083F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armRight", new AnimationChannel(Targets.POSITION,
          new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 1.4F, -5.7F), Interpolations.LINEAR),
          new Keyframe(0.4583F, KeyframeAnimations.posVec(2.0F, 1.4F, -5.7F), Interpolations.LINEAR),
          new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 1.4F, -5.7F), Interpolations.CATMULLROM)
      ))
      .build();

  public static final AnimationDefinition HEAR_NO_EVIL = Builder.withLength(4.0F)
      .addAnimation("body", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), Interpolations.LINEAR),
          new Keyframe(3.375F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), Interpolations.LINEAR),
          new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("body", new AnimationChannel(Targets.POSITION,
          new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 2.62F, -1.24F), Interpolations.CATMULLROM),
          new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, 2.0F, -4.0F), Interpolations.LINEAR),
          new Keyframe(3.375F, KeyframeAnimations.posVec(0.0F, 2.0F, -4.0F), Interpolations.LINEAR),
          new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
      ))
      .addAnimation("head", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(0.875F, KeyframeAnimations.degreeVec(7.8116F, -0.7309F, 0.1309F), Interpolations.CATMULLROM),
          new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), Interpolations.LINEAR),
          new Keyframe(3.0833F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), Interpolations.LINEAR),
          new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("head", new AnimationChannel(Targets.POSITION,
          new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 5.0F, 11.0F), Interpolations.LINEAR),
          new Keyframe(3.0833F, KeyframeAnimations.posVec(0.0F, 5.0F, 11.0F), Interpolations.LINEAR),
          new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armLeft", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-157.5F, 0.0F, 0.0F), Interpolations.LINEAR),
          new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-157.5F, 0.0F, 0.0F), Interpolations.LINEAR),
          new Keyframe(3.2083F, KeyframeAnimations.degreeVec(-155.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armLeft", new AnimationChannel(Targets.POSITION,
          new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(1.0417F, KeyframeAnimations.posVec(0.0F, -23.0F, -3.0F), Interpolations.LINEAR),
          new Keyframe(3.375F, KeyframeAnimations.posVec(0.0F, -23.0F, -3.0F), Interpolations.LINEAR),
          new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armRight", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(1.0F, KeyframeAnimations.degreeVec(-157.5F, 0.0F, 0.0F), Interpolations.LINEAR),
          new Keyframe(2.9167F, KeyframeAnimations.degreeVec(-157.5F, 0.0F, 0.0F), Interpolations.LINEAR),
          new Keyframe(3.2917F, KeyframeAnimations.degreeVec(-155.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .addAnimation("armRight", new AnimationChannel(Targets.POSITION,
          new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -23.0F, -3.0F), Interpolations.LINEAR),
          new Keyframe(3.375F, KeyframeAnimations.posVec(0.0F, -23.0F, -3.0F), Interpolations.LINEAR),
          new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
      ))
      .build();

  public static final AnimationDefinition IDLE = Builder.withLength(7.4074F).looping()
      .addAnimation("head", new AnimationChannel(Targets.ROTATION,
          new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), Interpolations.CATMULLROM),
          new Keyframe(1.8518F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(3.7037F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), Interpolations.CATMULLROM),
          new Keyframe(5.5556F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), Interpolations.CATMULLROM),
          new Keyframe(7.4074F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), Interpolations.CATMULLROM)
      ))
      .build();
}

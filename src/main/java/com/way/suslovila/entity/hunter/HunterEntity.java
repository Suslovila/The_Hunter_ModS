package com.way.suslovila.entity.hunter;

import com.way.suslovila.entity.ModEntityTypes;
import com.way.suslovila.entity.hunter.teleport.HunterTeleportFormEntity;
import com.way.suslovila.entity.projectile.explosionArrow.ExplosionArrow;
import com.way.suslovila.entity.projectile.speedArrow.SpeedArrow;
import com.way.suslovila.savedData.HuntersHP;
import com.way.suslovila.savedData.IsTheVictim.MessagesBoolean;
import com.way.suslovila.savedData.IsTheVictim.PacketSyncVictimToClientBoolean;
import com.way.suslovila.savedData.SaveVictim;
import com.way.suslovila.savedData.arrow.MessagesForArrow;
import com.way.suslovila.savedData.arrow.PacketSpawnArrow;
import com.way.suslovila.savedData.clientSynch.Messages;
import com.way.suslovila.savedData.clientSynch.PacketSyncRainyAuraToClient;
import com.way.suslovila.savedData.clientSynch.PacketSyncVictimToClient;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.processor.IBone;

import javax.annotation.Nullable;
import java.util.*;


public class HunterEntity extends PathfinderMob implements IAnimatable, IAnimationTickable {

    private static final EntityDataAccessor<String> UUIDOFGRABENTITY = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<Boolean> ISSTUCKED = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ISSHOOTING = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHOULDROTATEHANDSFORSHOOTING = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TIMER_FOR_PREPARING = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TIMER_FOR_SHOOTING = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    //I need to separate phases of animation: the start and the end. the end should loop.
    private static final EntityDataAccessor<Boolean> LOOPSHOOT = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LOOPBREATH = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TIMER_FOR_FALLING = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SHOULDLOOPCONTROLLINGSHADOWS = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TIMER_FOR_SUMMONING_SHADOWS = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ISSUMMONINGSHADOWS = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.BOOLEAN);


    private double XVictimPos = 0;
    private double YVictimPos = 0;
    private double ZVictimPos = 0;



    private static final EntityDataAccessor<Float> XCoord = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> YCoord = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ZCoord = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.FLOAT);

    private AnimationFactory factory = new AnimationFactory(this);


    public HunterEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

    }

    public static AttributeSupplier setAttributes() {

        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 16.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10D)
                .build();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
//        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(6, (new HurtByTargetGoal(this)));
    }



    private <E extends IAnimatable> PlayState predicateForUnderLight(AnimationEvent<E> event) {
//        System.out.println(event.getController().getAnimationState());
//        if (this.getVulnarble()) {
//            if (event.getController().getCurrentAnimation() != null) {
////                System.out.println(event.getController().getCurrentAnimation().animationName);
//                if ((Objects.equals(event.getController().getCurrentAnimation().animationName, "hunter.animation.underlight") && event.getController().getAnimationState().equals(AnimationState.Stopped)) || (Objects.equals(event.getController().getCurrentAnimation().animationName, "hunter.animation.underlightbreath") && !event.getController().getAnimationState().equals(AnimationState.Stopped))) {
//                    event.getController().setAnimation(new AnimationBuilder().addAnimation("hunter.animation.underlightbreath", true));
//                    return PlayState.CONTINUE;
//                }
//            }
////            System.out.println("vulnarable status");
//                event.getController().setAnimation(new AnimationBuilder().addAnimation("hunter.animation.underlight", false));
//            return PlayState.CONTINUE;


        if(this.getVulnarble()) {
            if(!getShouldLoopBreath()){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("hunter.animation.underlight", false));
                    return PlayState.CONTINUE;
            }
            else{
                event.getController().setAnimation(new AnimationBuilder().addAnimation("hunter.animation.underlightbreath", true));
                return PlayState.CONTINUE;
            }
        }

        return PlayState.STOP;

    }


    private <E extends IAnimatable> PlayState predicateForShooting(AnimationEvent<E> event) {

//        if (getShooting()) {
//            if (event.getController().getCurrentAnimation() != null) {
//                if ((Objects.equals(event.getController().getCurrentAnimation().animationName, "hunter.animation.preparetoshoot") && event.getController().getAnimationState().equals(AnimationState.Stopped)) || (Objects.equals(event.getController().getCurrentAnimation().animationName, "hunter.animation.shoot")&& !event.getController().getAnimationState().equals(AnimationState.Stopped))) {
//                    event.getController().setAnimation(new AnimationBuilder().addAnimation("hunter.animation.shoot", true));
//                    if (event.getAnimationTick() - event.getController().tickOffset > 54 && event.getAnimationTick() - event.getController().tickOffset<66){
//                         setShouldRotateHandsForShooting(true);
//                    }
//                    else{
//                        setShouldRotateHandsForShooting(false);
//                    }
//                    if((event.getAnimationTick() - event.getController().tickOffset == 64 || event.getAnimationTick() - event.getController().tickOffset == 65)){
//                        MessagesForArrow.sendToServer(new PacketSpawnArrow(true));
//                        System.out.println("sendedInfo");
//                    }
//                    return PlayState.CONTINUE;
//                }
//
//            }
//                event.getController().setAnimation(new AnimationBuilder().addAnimation("hunter.animation.preparetoshoot", false));
//                return PlayState.CONTINUE;
//        }

        if(getShooting()) {
            if (!getShouldLoopShoot()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("hunter.animation.preparetoshoot", false));
                return PlayState.CONTINUE;
            }
            if(getShouldLoopShoot()) {
                if (((HunterEntity) event.getAnimatable()).getTimeForShooting() >= 0 && ((HunterEntity) event.getAnimatable()).getTimeForShooting() < 13) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("hunter.animation.shootPhase1", false));
                    return PlayState.CONTINUE;
                }
                if (((HunterEntity) event.getAnimatable()).getTimeForShooting() >= 13 && ((HunterEntity) event.getAnimatable()).getTimeForShooting() < 37) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("hunter.animation.shootPhase2", false));
                    return PlayState.CONTINUE;
                }
                if (((HunterEntity) event.getAnimatable()).getTimeForShooting() >= 37 && ((HunterEntity) event.getAnimatable()).getTimeForShooting() < 54) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("hunter.animation.shootPhase3", false));
                    return PlayState.CONTINUE;
                }
                if (((HunterEntity) event.getAnimatable()).getTimeForShooting() >= 54 && ((HunterEntity) event.getAnimatable()).getTimeForShooting() <= 73) {
                    if(((HunterEntity) event.getAnimatable()).getTimeForShooting() < 70){
                        setShouldRotateHandsForShooting(true);
                    }
                    else{
                        setShouldRotateHandsForShooting(false);
                    }
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("hunter.animation.shootPhase4", false));
                    return PlayState.CONTINUE;
                }
            }
        }
        if(!getShooting() || event.getController().getAnimationState().equals(PlayState.STOP)){
            setShouldRotateHandsForShooting(false);
        }
        return PlayState.STOP;
    }
    @SuppressWarnings("resource")
    private <ENTITY extends IAnimatable> void customListener1(CustomInstructionKeyframeEvent<ENTITY> event) {
        MessagesForArrow.sendToHunter(new PacketSpawnArrow(true), this);
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController controller1 = new AnimationController(this, "controllerforshooting",
                0, this::predicateForShooting);
        AnimationController controller2 = new AnimationController(this, "controllerforunderlight",
                0, this::predicateForUnderLight);
        controller1.registerCustomInstructionListener(this::customListener1);
        data.addAnimationController(controller1);
        data.addAnimationController(controller2);
    }


    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
    //effects cannot be applied to Hunter
    @Override
    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        return false;
    }
    @Override
    public void baseTick() {
        super.baseTick();
//        System.out.println(getVulnarble());
//        System.out.println(getShooting());
//        System.out.println(this.getXRot());
//        System.out.println(this.getYRot());
//        System.out.println(this.getYHeadRot());
        if (!level.isClientSide()) {
           Messages.sendRainyAuraInfo(new PacketSyncRainyAuraToClient(new ArrayList<>(), new ArrayList<>()), this);
//working with arrows:
//            System.out.println("Is shooting: " + getShooting());
//            System.out.println("Timer for shooting: " + getTimeForShooting());
//            System.out.println("Timer for preparing: " + getTimerForPreparing());
//            System.out.println("Should loopShoot: " + getShouldLoopShoot());

      HuntersHP.get(this.level).changeHP(this.getHealth());



//            System.out.println(HuntersHP.get(this.level).getHunterHP());
            if (SaveVictim.get(this.level).getVictim().equals("novictim")) {
                this.disappearInShadows();
            } else{
             if (this.getBrightness() > 0.75f) {
                 setVulnarable(true);
                  setShooting(false);
                  setTimeForShooting(0);
                  setShouldLoopShoot(false);
                  setTimerForPreparing(0);
                  setTimerForSummoningShadows(0);
                  setIssummoningshadows(false);
                  setShouldloopcontrollingshadows(false);
           } else {
                 if (getTimeForShooting() == 72 && level.getPlayerByUUID(UUID.fromString(SaveVictim.get(level).getVictim())).distanceToSqr(XVictimPos, YVictimPos, ZVictimPos) < 0.1) {
                     setIssummoningshadows(true);

                     setVulnarable(false);
                     setShouldLoopBreath(false);
                     setTimerForFalling(0);

                     setShooting(false);
                     setShouldLoopShoot(false);
                     setTimerForPreparing(0);
                     setTimeForShooting(0);

                 } else {
                     setVulnarable(false);
                     setShouldLoopBreath(false);
                     setTimerForFalling(0);
                     setTimerForSummoningShadows(0);
                     setIssummoningshadows(false);
                     setShouldloopcontrollingshadows(false);

                 }
             }
                boolean isVictimHere = false;
                List<Entity> entities = level.getEntities(this, new AABB(this.getX() - 50.0D, this.getY() - 50.0D, this.getZ() - 50.0D, this.getX() + 50.0D, this.getY() + 50.0D, this.getZ() + 50.0D), EntitySelector.LIVING_ENTITY_STILL_ALIVE);
                for (int i = 0; i < entities.size(); i++) {
                    if (entities.get(i) instanceof Player) {
                        if(!SaveVictim.get(this.level).getVictim().equals("novictim")) {
                            if (UUID.fromString(SaveVictim.get(this.level).getVictim()).equals(((Player) entities.get(i)).getUUID())) {
                                isVictimHere = true;
                                if (getTimeForShooting() == 61) {
                                    Player player = (Player) entities.get(i);
                                    EntityAnchorArgument.Anchor pAnchor = EntityAnchorArgument.Anchor.FEET;
                                    Vec3 vec3 = pAnchor.apply(this);
                                    Vec3 Ptarget = player.position();
                                    double dx = Ptarget.x - vec3.x;
                                    double dz = Ptarget.z - vec3.z;
                                    double xz = Math.sqrt(dx * dx + dz * dz);
                                    double dy = Ptarget.y - (vec3.y + 3.1D);
                                    double cosV = Math.cos((float) (Math.atan2(dy, xz)) * 1.3f);
                                    double xzArmLength = 0.3D + 1.5 * cosV;
                                    double bodyRotAngle = (float) (Math.atan2(dx, dz) + 0.05f);
                                    double arrowXPos = vec3.x + xzArmLength * Math.sin(bodyRotAngle);
                                    double arrowZpos = vec3.z + xzArmLength * Math.cos(bodyRotAngle);
                                    double arrowYpos = vec3.y + 3.4 + 1.5 * Math.sin((float) (Math.atan2(dy, xz)) * 1.3f);

                                    Iterable<BlockPos> blocksBetween = BlockPos.betweenClosed(new BlockPos(arrowXPos, arrowYpos, arrowZpos), new BlockPos(player.position().x, player.getEyeY(), player.position().z));
                                    Iterator<BlockPos> iterator = blocksBetween.iterator();
                                    int countForBlocks = 0;
                                    boolean isBadBlockThere = false;
                                    ArrayList<Block> blocks = new ArrayList<>();
                                    while (iterator.hasNext()) {
                                        BlockPos block = iterator.next();
                                        if(!(this.level.getBlockState(block).isAir())) {
                                            countForBlocks++;
                                            blocks.add(this.level.getBlockState(block).getBlock());
                                            if (this.level.getBlockState(block).getBlock().getExplosionResistance() >=Blocks.OBSIDIAN.getExplosionResistance()) {
                                                isBadBlockThere = true;
                                            }
                                        }
                                    }
                                    System.out.println(countForBlocks);
                                    System.out.println(blocks);
                                    if (blocks.size() == 0 || BehaviorUtils.canSee(this, player)) {
                                        SpeedArrow arrow = new SpeedArrow(ModEntityTypes.SPEED_ARROW.get(), this.level);
                                        arrow.setPos(arrowXPos, arrowYpos, arrowZpos);
                                        Vec3 destination = new Vec3(player.getX() - arrowXPos, player.getY() + 1.5F - arrowYpos, player.getZ() - arrowZpos);
                                        arrow.setDeltaMovement(destination.scale(0.1f));
                                        this.level.addFreshEntity(arrow);
                                        arrow.setXCoordToAim((float) player.getX());
                                        arrow.setYCoordToAim((float) player.getY());
                                        arrow.setZCoordToAim((float) player.getZ());

                                    }
                                    else{
                                        if(blocks.size() < 9 && !isBadBlockThere){
                                            ExplosionArrow arrow = new ExplosionArrow(ModEntityTypes.EXPLOSION_ARROW.get(), this.level);
                                            arrow.setPos(arrowXPos, arrowYpos, arrowZpos);
                                            Vec3 destination = new Vec3(player.getX() - arrowXPos, player.getY() + 1.5F - arrowYpos, player.getZ() - arrowZpos);
                                            arrow.setDeltaMovement(destination.scale(0.3f));
                                            this.level.addFreshEntity(arrow);
                                            arrow.setXCoordToAim((float) player.getX());
                                            arrow.setYCoordToAim((float) player.getY());
                                            arrow.setZCoordToAim((float) player.getZ());
                                        }
                                    }
                                }
                            }
                        }
                        if (entities.get(i).distanceTo(this) < 0) {if(!getVulnarble()) this.disappearInShadows();}
                    }

                }
                if (!isVictimHere) {
                    this.disappearInShadows();
                }
                if (isVictimHere) {
                    if(!getVulnarble() && !getIsSummoningShadows()) {
                        Messages.sendToHunter(new PacketSyncVictimToClient(UUID.fromString(SaveVictim.get(this.level).getVictim())), this);
                        MessagesBoolean.sendToHunter(new PacketSyncVictimToClientBoolean(true), this);
                        Player player = level.getPlayerByUUID(UUID.fromString(SaveVictim.get(this.level).getVictim()));
                        setXCoordToAim((float)player.getEyePosition().x);
                        setYCoordToAim((float)player.getEyePosition().y);
                        setZCoordToAim((float)player.getEyePosition().z);

                        setShooting(true);
                    }
                }
            }


            if(getShooting()){
                if(!getShouldLoopShoot()) {
                    setTimerForPreparing(getTimerForPreparing() + 1);
                    if(getTimerForPreparing() >= 5){
                        setShouldLoopShoot(true);
                        setTimerForPreparing(0);
                    }
                }
                if(getShouldLoopShoot()){
                    setTimeForShooting((getTimeForShooting()+1));
                    if(getTimeForShooting()==73){
                        setTimeForShooting(0);
                    }
                }
            }
            if(getVulnarble()){
                if(!getShouldLoopBreath()){
                    setTimerForFalling(getTimerForFalling() + 1);
                    if(getTimerForFalling() == 24){
                        setShouldLoopBreath(true);
                        setTimerForFalling(0);
                    }

                }
            }
        }
    }





//does not take magic damage
@Override
public boolean hurt(DamageSource pSource, float pAmount) {
        if(!level.isClientSide()) {
            System.out.println("Running Hurt method");
            if (pSource.isMagic()) {
                System.out.println("not Magic damage");
                return false;
            } else {
                if (!getVulnarble()) {
                    int number = random.nextInt(3);
                    if (number == 2 || number == 1) {
                        System.out.println("reduced damage applied");
                        return super.hurt(pSource, pAmount * 0.1f);
                    }
                    if (number == 0) {
                        this.disappearInShadows();
                        System.out.println("Hiding in shadows");
                        return false;
                    }
                } else {
                    return super.hurt(pSource, pAmount);
                }
                return super.hurt(pSource, pAmount);
            }
        }
    return super.hurt(pSource, pAmount);
}

        @Override
        public int tickTimer() {
            return tickCount;
        }

    class AvoidLightGoal extends Goal{

    @Override
    public boolean canUse() {
        return false;
    }
}

class EscapePlayer extends Goal{

    @Override
    public boolean canUse() {
return true;
    }
}


public void disappearInShadows(){
    HunterTeleportFormEntity hunterTeleportFormEntity = new HunterTeleportFormEntity(ModEntityTypes.HUNTER_TELEPORT_FORM.get(), this.level);
    hunterTeleportFormEntity.moveTo(this.getX(), this.getY(), this.getZ(), 0, 0);
    this.level.addFreshEntity(hunterTeleportFormEntity);
        this.discard();

}
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(UUIDOFGRABENTITY, "NoGrabEntity");
        getEntityData().define(ISSTUCKED, false);
        getEntityData().define(ISSHOOTING, false);
        getEntityData().define(SHOULDROTATEHANDSFORSHOOTING, false);
        getEntityData().define(TIMER_FOR_PREPARING, 0);
        getEntityData().define(TIMER_FOR_SHOOTING, 0);
        getEntityData().define(LOOPSHOOT, false);
        getEntityData().define(LOOPBREATH, false);
        getEntityData().define(TIMER_FOR_FALLING,0);
        getEntityData().define(XCoord, 0f);
        getEntityData().define(ZCoord, 0f);
        getEntityData().define(YCoord, 0f);

        getEntityData().define(TIMER_FOR_SUMMONING_SHADOWS,0);
        getEntityData().define(ISSUMMONINGSHADOWS,false);
        getEntityData().define(SHOULDLOOPCONTROLLINGSHADOWS,false);


    }
    //lots of data methods
    public boolean getShouldLoopContollingShadows(){
        return getEntityData().get(SHOULDLOOPCONTROLLINGSHADOWS);
    }
    public void setShouldloopcontrollingshadows(boolean bool){getEntityData().set(SHOULDLOOPCONTROLLINGSHADOWS, bool);}
    public int getTimerForSummoningShoadows(){return getEntityData().get(TIMER_FOR_SUMMONING_SHADOWS);}
    public void setTimerForSummoningShadows(int rot){getEntityData().set(TIMER_FOR_SUMMONING_SHADOWS, rot);}
    public boolean getIsSummoningShadows(){
        return getEntityData().get(ISSUMMONINGSHADOWS);
    }
    public void setIssummoningshadows(boolean bool){getEntityData().set(ISSUMMONINGSHADOWS, bool);}
    public int getTimerForFalling(){
        return getEntityData().get(TIMER_FOR_FALLING);
    }
    public void setTimerForFalling(int rot){
        getEntityData().set(TIMER_FOR_FALLING, rot);
    }
    public boolean getShouldLoopShoot(){
        return getEntityData().get(LOOPSHOOT);
    }
    public void setShouldLoopShoot(boolean bool){
         getEntityData().set(LOOPSHOOT, bool);
    }
    public boolean getShouldLoopBreath(){
        return getEntityData().get(LOOPBREATH);
    }
    public void setShouldLoopBreath(boolean bool){
        getEntityData().set(LOOPBREATH, bool);
    }
    public boolean getShouldRotateHandsForShooting(){
        return getEntityData().get(SHOULDROTATEHANDSFORSHOOTING);
    }
    public void setShouldRotateHandsForShooting(boolean bool){
        getEntityData().set(SHOULDROTATEHANDSFORSHOOTING, bool);
    }
    public int getTimerForPreparing(){
        return getEntityData().get(TIMER_FOR_PREPARING);
    }
    public void setTimerForPreparing(int rot){
        getEntityData().set(TIMER_FOR_PREPARING, rot);
    }
    public int getTimeForShooting(){
        return getEntityData().get(TIMER_FOR_SHOOTING);
    }

    public void setTimeForShooting(int rot){
        getEntityData().set(TIMER_FOR_SHOOTING, rot);
    }
    public boolean getShooting() {
        return getEntityData().get(ISSHOOTING);
    }

    public void setShooting(boolean shooting) {
        getEntityData().set(ISSHOOTING, shooting);
    }
    public boolean getVulnarble() {
        return getEntityData().get(ISSTUCKED);
    }

    public void setVulnarable(boolean stucked) {
        getEntityData().set(ISSTUCKED, stucked);
    }
    public float getXCoordToAim(){
        return getEntityData().get(XCoord);
    }
    public float getYCoordToAim(){
        return getEntityData().get(YCoord);
    }
    public float getZCoordToAim(){
        return getEntityData().get(ZCoord);
    }
    public void setXCoordToAim(float coord){
        getEntityData().set(XCoord, coord);
    }
    public void setYCoordToAim(float coord){
        getEntityData().set(YCoord, coord);
    }
    public void setZCoordToAim(float coord){
        getEntityData().set(ZCoord, coord);
    }
    public void lookAtVictim(EntityAnchorArgument.Anchor pAnchor, Vec3 pTarget, IBone body, IBone head) {
        Vec3 vec3 = pAnchor.apply(this);
        double dx = pTarget.x - vec3.x;
        double dz = pTarget.z - vec3.z;
        double xz = Math.sqrt(dx * dx + dz * dz);
        double dy = pTarget.y-0.22D - (vec3.y+3);


        body.setRotationY(Mth.wrapDegrees((float)(Math.atan2(dx,dz))));
        head.setRotationX(Mth.wrapDegrees((float)(Math.atan2(dy,xz))));
////    bone.setRotationY(Mth.wrapDegrees((float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F));
        //this.setYHeadRot(Mth.wrapDegrees(-(float)(Mth.atan2(dz, dx) * (double)(180F / (float)Math.PI))));
        //  this.setYHeadRot((float)(Mth.atan2(dz, dx) * (double)(180F / (float)Math.PI)));
        // this.yRotO = this.getYRot();

    }
    public void aimBowAtVictim(EntityAnchorArgument.Anchor pAnchor, Vec3 pTarget, IBone leftArm, IBone rightArm, IBone palm){
        Vec3 vec3 = pAnchor.apply(this);
        double dx = pTarget.x - vec3.x;
        double dz = pTarget.z - vec3.z;
        double xz = Math.sqrt(dx * dx + dz * dz);
        double dy = pTarget.y - (vec3.y+3.1D);

        rightArm.setRotationX(rightArm.getRotationX() + (Mth.wrapDegrees((float)(Math.atan2(dy,xz))))*1.3f);
        //this
//        rightArm.setRotationY(rightArm.getRotationY() + (Mth.wrapDegrees((float)(Math.atan2(dy,xz)))*0.57F/(-5.25F)*5f));
//        rightArm.setRotationZ(rightArm.getRotationZ() + (Mth.wrapDegrees((float)(Math.atan2(dy,xz)))*1.72F/(-5.25F)*0.8f));


        palm.setRotationX(palm.getRotationX() - Mth.wrapDegrees((float)(Math.atan2(dy,xz)))/2.5f);
        palm.setRotationY((palm.getRotationY() - (Mth.wrapDegrees((float)(Math.atan2(dy,xz)))/2.5f)*1.48f/4.73f));
        palm.setRotationZ((palm.getRotationZ() - (Mth.wrapDegrees((float)(Math.atan2(dy,xz)))/2.5f)*0.7f/4.73f*(-1)));

        leftArm.setRotationX(leftArm.getRotationX() + Mth.wrapDegrees((float)(Math.atan2(dy,xz)))/3f);
        leftArm.setRotationY((leftArm.getRotationY() + (Mth.wrapDegrees((float)(Math.atan2(dy,xz)))/4.2f)*7.16f/2.31f));
        leftArm.setRotationZ((leftArm.getRotationZ() + (Mth.wrapDegrees((float)(Math.atan2(dy,xz)))/4f)*0.36f/2.31f*(-1)));

    }
}



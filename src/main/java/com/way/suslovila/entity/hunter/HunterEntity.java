package com.way.suslovila.entity.hunter;

import com.way.suslovila.entity.ModEntityTypes;
import com.way.suslovila.entity.hunter.teleport.HunterTeleportFormEntity;
import com.way.suslovila.entity.projectile.explosionArrow.ExplosionArrow;
import com.way.suslovila.entity.projectile.speedArrow.SpeedArrow;
import com.way.suslovila.entity.shadowGrabEntity.ShadowGrabEntity;
import com.way.suslovila.particles.TailBlackParticles;
import com.way.suslovila.savedData.DelayBeforeSpawningHunter;
import com.way.suslovila.savedData.HuntersHP;
import com.way.suslovila.savedData.SaveVictim;
import com.way.suslovila.savedData.clientSynch.Messages;
import com.way.suslovila.savedData.clientSynch.PacketSyncVictimToClient;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.processor.IBone;

import javax.annotation.Nullable;
import java.util.*;


public class HunterEntity extends PathfinderMob implements IAnimatable, IAnimationTickable {
    //todo: запретить двигать камерой; сделать хотя бы какую-то анимацию во время контроля теней
    //max light amount Hunter can stand:
        public static float maxLight = 0.26f;
        //variables for storing time of any Hunter's action
   private HashMap<Vec3, ArrayList<Object>> cordsForShadowsAroundHand = new HashMap<>();
    private static final EntityDataAccessor<String> UUIDOFGRABENTITY = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> SHOULDROTATEHANDSFORSHOOTING = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TIMER_FOR_SUMMONING_SHADOWS = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> ACTUAL_TASK = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> TIMER_FOR_PREPARING = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SHOOTPHASE1 = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SHOOTPHASE2 = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SHOOTPHASE3 = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SHOOTPHASE4 = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> TIMER_FOR_FALLING = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TIMER_FOR_VULNARABLE = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> TIMER_FOR_CONTROLLING_SHADOWS = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TIMER_FOR_SHADOW_MONSTER = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);


    //binding timers to their names
   private final static HashMap<String, EntityDataAccessor<Integer>> actionsMap  = new HashMap<String, EntityDataAccessor<Integer>>() {{
        put("prepareForShoot", TIMER_FOR_PREPARING);
        put("shootPhase1", SHOOTPHASE1);
        put("shootPhase2", SHOOTPHASE2);
        put("shootPhase3", SHOOTPHASE3);
        put("shootPhase4", SHOOTPHASE4);
        put("falling",TIMER_FOR_FALLING);
        put("vulnarable",TIMER_FOR_VULNARABLE);
        put("summonShadows",TIMER_FOR_SUMMONING_SHADOWS);
        put("controlShadows",TIMER_FOR_CONTROLLING_SHADOWS);
        put("shadowMonster", TIMER_FOR_SHADOW_MONSTER);
    }};
   //storing victim's position for rotations and aiming
    private static final EntityDataAccessor<Float> XCoord = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> YCoord = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ZCoord = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.FLOAT);
    private AnimationFactory factory = new AnimationFactory(this);

    public HunterEntity(EntityType<? extends PathfinderMob> entityType, Level level) {super(entityType, level);}

    public static AttributeSupplier setAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 16.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10D)
                .build();
    }

    protected void registerGoals() {}
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("hunter.animation."+getEntityData().get(ACTUAL_TASK)));
        System.out.println("In predicate: " + getEntityData().get(ACTUAL_TASK));
            return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimationData data) {
        AnimationController controller = new AnimationController(this, "HunterController",
                0, this::predicate);
        data.addAnimationController(controller);

    }
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
    @Override
    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        return false;
    }
    @Override
    public void baseTick() {
        super.baseTick();
        if (!level.isClientSide()) {
           System.out.println(getActualTask());
            System.out.println("Timer for PFS: " +getEntityData().get(TIMER_FOR_PREPARING));
            System.out.println("Timer for S1: " + getEntityData().get(SHOOTPHASE1));
            //Hunter's HP is connected with special storing variable in SavedData for whole world:
      HuntersHP.get(this.level).changeHP(this.getHealth());
            //if there is no victim in world:
            if (SaveVictim.get(this.level).getVictim().equals("novictim") || (DelayBeforeSpawningHunter.get(level).getHunterDelay() - HunterTeleportFormEntity.lifeTime - 5 <= 0 && DelayBeforeSpawningHunter.get(level).getHunterDelay() > - 1)) {
                this.disappearInShadows();
            } else{
                //if it is too bright for Hunter:
             if (this.getBrightness() > maxLight) {
                 if(!getActualTask().equals("vulnarable"))setActualTask("falling");
           } else {
                 Player player = Objects.requireNonNull(level.getPlayerByUUID(UUID.fromString(SaveVictim.get(level).getVictim())));
                 //if player is not moving, Hunter will try to catch him with shadows:
                 if (getEntityData().get(SHOOTPHASE4) == 18 && player.getEyePosition().distanceTo(new Vec3(getXCoordToAim(), getYCoordToAim(), getZCoordToAim()))< 0.1 && player.getBrightness() <= maxLight && level.getBlockState(new BlockPos(player.getBlockX(), player.getBlockY() - 1,player.getBlockZ())).getMaterial().blocksMotion()) {
                     if (random.nextInt(5) == 4) {

                     }else {
                         setActualTask("summonShadows");
                         ShadowGrabEntity shadowGrabEntity = new ShadowGrabEntity(ModEntityTypes.SHADOW_GRAB.get(), level);
                         shadowGrabEntity.getEntityData().set(ShadowGrabEntity.OWNER, this.getUUID().toString());
                         shadowGrabEntity.setPos(Objects.requireNonNull(level.getPlayerByUUID(UUID.fromString(SaveVictim.get(level).getVictim()))).position());
                         level.addFreshEntity(shadowGrabEntity);
                         setGrabUUID(shadowGrabEntity.getUUID().toString());
                     }
                 }

                    if(Objects.equals(getGrabUUID(), "NoGrabEntity") ||((ServerLevel)level).getEntity(UUID.fromString(getGrabUUID())) == null|| ((((ServerLevel)level).getEntity(UUID.fromString(getGrabUUID())))!= null &&!(((ServerLevel)level).getEntity(UUID.fromString(getGrabUUID()))).isAlive()) && isGrabbing()){
                        if(!isShooting()) setActualTask("prepareForShoot");
                    }
             }
             //Is victim here?
                boolean isVictimHere = false;
                List<Entity> entities = level.getEntities(this, new AABB(this.getX() - 40.0D, this.getY() - 40.0D, this.getZ() - 40.0D, this.getX() + 40.0D, this.getY() + 40.0D, this.getZ() + 40.0D), EntitySelector.LIVING_ENTITY_STILL_ALIVE);
                for (int i = 0; i < entities.size(); i++) {
                    if (entities.get(i) instanceof Player) {
                        if(!SaveVictim.get(this.level).getVictim().equals("novictim")) {
                            if (UUID.fromString(SaveVictim.get(this.level).getVictim()).equals(((Player) entities.get(i)).getUUID())) {
                                isVictimHere = true;
                                //if it's time to shoot arrow:
                                if (getEntityData().get(SHOOTPHASE4) == 7) {
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
                                    //depending on situation Hunter should decide if he need to shoot destroyable arrow:
                                    Iterable<BlockPos> blocksBetween = BlockPos.betweenClosed(new BlockPos(arrowXPos, arrowYpos, arrowZpos), new BlockPos(player.position().x, player.getEyeY(), player.position().z));
                                    Iterator<BlockPos> iterator = blocksBetween.iterator();
                                    int countForBlocks = 0;
                                    boolean isBadBlockThere = false;
                                    ArrayList<Block> blocks = new ArrayList<>();
                                    while (iterator.hasNext()) {
                                        BlockPos block = iterator.next();
                                        if(!(this.level.getBlockState(block).isAir())) {
                                            if(level.getBlockState(block).getMaterial().blocksMotion()) {
                                                countForBlocks++;
                                                blocks.add(this.level.getBlockState(block).getBlock());
                                            }
                                            if (this.level.getBlockState(block).getBlock().getExplosionResistance() >=Blocks.OBSIDIAN.getExplosionResistance()) {
                                                isBadBlockThere = true;
                                            }
                                        }
                                    }
                                    System.out.println(countForBlocks);
                                    System.out.println(blocks);
                                    if (blocks.size() == 0 || BehaviorUtils.canSee(this, player) || level.clip(new ClipContext(player.getEyePosition(), getEyePosition(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getType() == HitResult.Type.MISS) {
                                        SpeedArrow arrow = new SpeedArrow(ModEntityTypes.SPEED_ARROW.get(), this.level);
                                        arrow.setPos(arrowXPos, arrowYpos, arrowZpos);
                                        Vec3 destination = new Vec3(player.getX() - arrowXPos, player.getY() + 1.5F - arrowYpos, player.getZ() - arrowZpos);
                                        arrow.setDeltaMovement(destination.scale(0.1f));
                                        this.level.addFreshEntity(arrow);
                                        arrow.setXCoordToAim((float) player.getX());
                                        arrow.setYCoordToAim((float) player.getY());
                                        arrow.setZCoordToAim((float) player.getZ());

                                    }
                                    //if there are blocks:
                                    else{
                                        if(!isBadBlockThere){
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
                                if(Objects.equals(getActualTask(), "controlShadows")) {
                                    double armLength = 0.3D + 1.5;
                                    if (cordsForShadowsAroundHand.size() < 6) {
                                        double radius = random.nextDouble(0.15, 0.5);
                                        int timer = 0;
                                        Vec3 lookVectorNormal = this.getViewVector(0);
                                        Vec3 lookVector = lookVectorNormal.scale(armLength * random.nextDouble(0.35, 0.85));
                                        Vec3 m = new Vec3(lookVectorNormal.z, 0, -lookVectorNormal.x);
                                        m = m.normalize();
                                        Vec3 k = lookVectorNormal.cross(m);
                                        k = k.normalize();
                                        ArrayList<Object> arrayList = new ArrayList<Object>();
                                        arrayList.add(radius);
                                        arrayList.add(m);
                                        arrayList.add(k);
                                        arrayList.add(timer);
                                        cordsForShadowsAroundHand.put(new Vec3(this.position().x + lookVector.x + m.x*0.40, this.position().y + 2.7 + lookVector.y, this.position().z + lookVector.z+m.z*0.40), arrayList);
                                    }
                                    HashMap map = (HashMap) cordsForShadowsAroundHand.clone();
                                    Iterator<Vec3> iterator = map.keySet().iterator();
                                    while (iterator.hasNext()) {
                                        Vec3 dotInSpace = iterator.next();
                                        ArrayList<Object> list = cordsForShadowsAroundHand.get(dotInSpace);
                                        double radius = (double)list.get(0);
                                        Vec3 m = ((Vec3)list.get(1)).scale(radius);
                                        Vec3 k = ((Vec3)list.get(2)).scale(radius);
                                        for (int h = 0; h < 7; h++) {
                                            int timer = (int)list.get(3);
                                            Vec3 a = m.scale(Math.cos(timer * Math.PI / 100)).add(k.scale(Math.sin(timer * Math.PI / 100)));
                                            list.remove(3);
                                            list.add(timer+1);
                                            Vec3 endPosition = dotInSpace.add(a);
                                            ((ServerLevel) this.level).sendParticles(new TailBlackParticles.TailParticleData(random.nextDouble(0.03D, 0.05D), random.nextInt(19, 20)),
                                                    endPosition.x, endPosition.y, endPosition.z, 1, 0,
                                                    0, 0, 0);
                                            if (random.nextInt(60) == 37) {
                                                cordsForShadowsAroundHand.remove(dotInSpace);
                                            }
                                        }
                                    }
                                }
                                else{
                                    cordsForShadowsAroundHand.clear();
                                }



                            }
                        }
                        //if player is too near to Hunter:
                        if (entities.get(i).distanceTo(this) < 0) {if(!isVulnarable()) this.disappearInShadows();}
                    }

                }
                if (!isVictimHere) {
                    this.disappearInShadows();
                }
                if (isVictimHere) {
                    //rotation stuff if Hunter is not vulnarable:
                    if(!isVulnarable()) {
                        Messages.sendToHunter(new PacketSyncVictimToClient(UUID.fromString(SaveVictim.get(this.level).getVictim())), this);
//                        MessagesBoolean.sendToHunter(new PacketSyncVictimToClientBoolean(true), this);
                        Player player = level.getPlayerByUUID(UUID.fromString(SaveVictim.get(this.level).getVictim()));
                        assert player != null;
                        EntityAnchorArgument.Anchor pAnchor = EntityAnchorArgument.Anchor.FEET;
                        Vec3 pTarget = player.getEyePosition();
                        Vec3 vec3 = pAnchor.apply(this);
                        double dx = pTarget.x - vec3.x;
                        double dz = pTarget.z - vec3.z;
                        double angle = (-Math.toDegrees(((float)(Math.atan2(dx,dz)))));
                        this.setYBodyRot((float)angle);
                        this.getLookControl().setLookAt(player);
                        setXCoordToAim((float)player.getEyePosition().x);
                        setYCoordToAim((float)player.getEyePosition().y);
                        setZCoordToAim((float)player.getEyePosition().z);
                    }
                }
            }
            System.out.println("Task before check: " + getActualTask());
            //now we need to add time for current action and set to 0 all other timers
            HashMap<String, EntityDataAccessor<Integer>> map = (HashMap<String, EntityDataAccessor<Integer>>)actionsMap.clone();
                Iterator<String> iteratorForActions = map.keySet().iterator();
                while (iteratorForActions.hasNext()) {
                    String action = iteratorForActions.next();
                    String s = getActualTask();
                    if (Objects.equals(action, getActualTask())) getEntityData().set(actionsMap.get(action), getEntityData().get(actionsMap.get(action)) + 1);
                    else getEntityData().set(actionsMap.get(action), 0);
                }
                //when are supposed to end, some actions should start other particular ones
                if (getActualTask().equals("prepareForShoot") && getEntityData().get(TIMER_FOR_PREPARING) == 5)
                    setActualTask("shootPhase1");
                if (getActualTask().equals("shootPhase1") && getEntityData().get(SHOOTPHASE1) == 13)
                    setActualTask("shootPhase2");
                if (getActualTask().equals("shootPhase2") && getEntityData().get(SHOOTPHASE2) == 25)
                    setActualTask("shootPhase3");
                if (getActualTask().equals("shootPhase3") && getEntityData().get(SHOOTPHASE3) == 18)
                    setActualTask("shootPhase4");
                if (getActualTask().equals("shootPhase4") && getEntityData().get(SHOOTPHASE4) == 19)
                    setActualTask("shootPhase1");


                if (getActualTask().equals("summonShadows") && getEntityData().get(TIMER_FOR_SUMMONING_SHADOWS) >= 30)
                    setActualTask("controlShadows");


                if (getActualTask().equals("falling") && getEntityData().get(TIMER_FOR_FALLING) == 24)
                    setActualTask("vulnarable");

            System.out.println("Task at the end: " + getActualTask());
            //should we rotate Hunter's hands?
           if(getActualTask().equals("shootPhase4") && getEntityData().get(SHOOTPHASE4) < 16){
               setShouldRotateHandsForShooting(true);
           }
           else{
               setShouldRotateHandsForShooting(false);
           }



        }
    }



public boolean isVulnarable(){
        return getActualTask().equals("vulnarable") || getActualTask().equals("falling");
}
private boolean isShooting(){
        return  getActualTask().equals("shootPhase1") || getActualTask().equals("shootPhase2")||getActualTask().equals("shootPhase3")||getActualTask().equals("shootPhase4");
}
    private boolean isGrabbing(){
        return getActualTask().equals("controlShadows") || getActualTask().equals("summonShadows");
    }
//does not take magic damage
@Override
public boolean hurt(DamageSource pSource, float pAmount) {
        //when attacked and not vulnarable, Hunter will reduce incoming damage or, with chance, hide in shadows:
        if(!level.isClientSide()) {
            if (pSource.isMagic()) {
                return false;
            } else {
                if (!isVulnarable()) {
                    int number = random.nextInt(3);
                    if (number == 2 || number == 1) {
                        return super.hurt(pSource, pAmount * 0.1f);
                    }
                    if (number == 0) {
                        this.disappearInShadows();
                        DelayBeforeSpawningHunter.get(level).changeTime(HunterTeleportFormEntity.lifeTime);
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
public void disappearInShadows(){
    HunterTeleportFormEntity hunterTeleportFormEntity = new HunterTeleportFormEntity(ModEntityTypes.HUNTER_TELEPORT_FORM.get(), this.level);
    hunterTeleportFormEntity.moveTo(this.getX(), this.getY(), this.getZ(), 0, 0);
    this.level.addFreshEntity(hunterTeleportFormEntity);
        this.discard();
//        if(!level.isClientSide()){
//            DelayBeforeSpawningHunter.get(level).changeTime(HunterTeleportFormEntity.lifeTime);
//        }

}
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(UUIDOFGRABENTITY, "NoGrabEntity");
        getEntityData().define(SHOULDROTATEHANDSFORSHOOTING, false);
        getEntityData().define(TIMER_FOR_PREPARING, 0);
        getEntityData().define(TIMER_FOR_FALLING,0);
        getEntityData().define(XCoord, 0f);
        getEntityData().define(ZCoord, 0f);
        getEntityData().define(YCoord, 0f);
        getEntityData().define(TIMER_FOR_SUMMONING_SHADOWS,0);
        getEntityData().define(TIMER_FOR_CONTROLLING_SHADOWS,0);
getEntityData().define(ACTUAL_TASK, "noAction");
        getEntityData().define(SHOOTPHASE1, 0);
        getEntityData().define(SHOOTPHASE2, 0);
        getEntityData().define(SHOOTPHASE3, 0);
        getEntityData().define(SHOOTPHASE4, 0);
        getEntityData().define(TIMER_FOR_VULNARABLE, 0);
        getEntityData().define(TIMER_FOR_SHADOW_MONSTER, 0);

    }
    //lots of data methods
    public String getGrabUUID(){return (getEntityData().get(UUIDOFGRABENTITY));}
    public void setGrabUUID(String uuid){getEntityData().set(UUIDOFGRABENTITY, uuid);}
    public boolean getShouldRotateHandsForShooting(){
        return getEntityData().get(SHOULDROTATEHANDSFORSHOOTING);
    }
    public void setShouldRotateHandsForShooting(boolean bool){
        getEntityData().set(SHOULDROTATEHANDSFORSHOOTING, bool);
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
    public void setActualTask(String task){
        getEntityData().set(ACTUAL_TASK, task);
    }
    public String getActualTask(){
        return getEntityData().get(ACTUAL_TASK);
    }

    public void lookAtVictim(EntityAnchorArgument.Anchor pAnchor, Vec3 pTarget, IBone body, IBone head) {

        if(!Objects.equals(getActualTask(), "summonShadows")) {
            Vec3 vec3 = pAnchor.apply(this);
            double dx = pTarget.x - vec3.x;
            double dz = pTarget.z - vec3.z;
            double xz = Math.sqrt(dx * dx + dz * dz);
            double dy = pTarget.y - 0.22D - (vec3.y + 3);
            head.setRotationX(Mth.wrapDegrees((float) (Math.atan2(dy, xz))));
            double angle = (-Math.toDegrees(((float) (Math.atan2(dx, dz)))));
            this.lerpYRot = (float) angle;
            this.setYBodyRot((float) angle);

        }
    }

    public void aimBowAtVictim(EntityAnchorArgument.Anchor pAnchor, Vec3 pTarget, IBone leftArm, IBone rightArm, IBone palm){
        Vec3 vec3 = pAnchor.apply(this);
        double dx = pTarget.x - vec3.x;
        double dz = pTarget.z - vec3.z;
        double xz = Math.sqrt(dx * dx + dz * dz);
        double dy = pTarget.y - (vec3.y+3.1D);
        if(getShouldRotateHandsForShooting()) {
            rightArm.setRotationX(rightArm.getRotationX() + (Mth.wrapDegrees((float) (Math.atan2(dy, xz)))) * 1.3f);
            //this
//        rightArm.setRotationY(rightArm.getRotationY() + (Mth.wrapDegrees((float)(Math.atan2(dy,xz)))*0.57F/(-5.25F)*5f));
//        rightArm.setRotationZ(rightArm.getRotationZ() + (Mth.wrapDegrees((float)(Math.atan2(dy,xz)))*1.72F/(-5.25F)*0.8f));


            palm.setRotationX(palm.getRotationX() - Mth.wrapDegrees((float) (Math.atan2(dy, xz))) / 2.5f);
            palm.setRotationY((palm.getRotationY() - (Mth.wrapDegrees((float) (Math.atan2(dy, xz))) / 2.5f) * 1.48f / 4.73f));
            palm.setRotationZ((palm.getRotationZ() - (Mth.wrapDegrees((float) (Math.atan2(dy, xz))) / 2.5f) * 0.7f / 4.73f * (-1)));

            leftArm.setRotationX(leftArm.getRotationX() + Mth.wrapDegrees((float) (Math.atan2(dy, xz))) / 3f);
            leftArm.setRotationY((leftArm.getRotationY() + (Mth.wrapDegrees((float) (Math.atan2(dy, xz))) / 4.2f) * 7.16f / 2.31f));
            leftArm.setRotationZ((leftArm.getRotationZ() + (Mth.wrapDegrees((float) (Math.atan2(dy, xz))) / 4f) * 0.36f / 2.31f * (-1)));
        }
        if(Objects.equals(getActualTask(), "controlShadows")){
            leftArm.setRotationX(leftArm.getRotationX() + (Mth.wrapDegrees((float)(Math.atan2(dy,xz))))*1.3f);
        }

    }
}



package com.way.suslovila.entity.hunter;

import com.way.suslovila.entity.ModEntityTypes;
import com.way.suslovila.entity.ShadowCreature;
import com.way.suslovila.entity.hunter.teleport.HunterTeleportFormEntity;
import com.way.suslovila.entity.projectile.explosionArrow.ExplosionArrow;
import com.way.suslovila.entity.projectile.speedArrow.SpeedArrow;
import com.way.suslovila.entity.shadowGrabEntity.ShadowGrabEntity;
import com.way.suslovila.entity.shadowMonster.ShadowMonsterEntity;
import com.way.suslovila.music.ModSounds;
import com.way.suslovila.particles.TailBlackParticles;
import com.way.suslovila.savedData.DelayBeforeSpawningHunter;
import com.way.suslovila.savedData.HuntersHP;
import com.way.suslovila.savedData.SaveVictim;
import com.way.suslovila.savedData.clientSynch.ClientVictimData;
import com.way.suslovila.savedData.clientSynch.Messages;
import com.way.suslovila.savedData.clientSynch.PacketSyncVictimToClient;
import com.way.suslovila.sounds.MCSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.PushReaction;
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


public class HunterEntity extends ShadowCreature implements IAnimatable, IAnimationTickable {

    //max light amount Hunter can stand:
        public static final float maxLight = 0.26f;
        public static final int minDistanceToPlayer = 6;
        public static final int maxDistanceToVictim = 40;
        //variables for storing time for Hunter's actions
   private HashMap<Vec3, ArrayList<Object>> cordsForShadowsHunter = new HashMap<>();
    private static final EntityDataAccessor<String> UUIDOFGRABENTITY = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> SHOULDROTATEHANDSFORSHOOTING = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TIMER_FOR_SUMMONING_SHADOWS = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> ACTUAL_TASK = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> TIMER_FOR_PREPARING = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SHOOTPHASE1 = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SHOOTPHASE2 = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SHOOTPHASE3 = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SHOOTPHASE4 = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);

    //private static final EntityDataAccessor<Integer> TIMER_FOR_FALLING = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    //private static final EntityDataAccessor<Integer> TIMER_FOR_VULNARABLE = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> TIMER_FOR_CONTROLLING_SHADOWS = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TIMER_FOR_SHADOW_MONSTER = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.INT);


    //binding timers to their names
   private final static HashMap<String, EntityDataAccessor<Integer>> actionsMap  = new HashMap<>() {{
        put("prepareForShoot", TIMER_FOR_PREPARING);
        put("shootPhase1", SHOOTPHASE1);
        put("shootPhase2", SHOOTPHASE2);
        put("shootPhase3", SHOOTPHASE3);
        put("shootPhase4", SHOOTPHASE4);
        //put("falling",TIMER_FOR_FALLING);
        //put("vulnarable",TIMER_FOR_VULNARABLE);
        put("summonShadows",TIMER_FOR_SUMMONING_SHADOWS);
        put("controlShadows",TIMER_FOR_CONTROLLING_SHADOWS);
        put("shadowMonster", TIMER_FOR_SHADOW_MONSTER);
    }};
    private final static HashMap<String, Double> animationSpeedMap  = new HashMap<String,Double>() {{
        put("prepareForShoot", 1.0);
        put("shootPhase1", 2.0);
        put("shootPhase2", 2.0);
        put("shootPhase3", 2.0);
        put("shootPhase4", 2.0);
        //put("falling",1.0);
        //put("vulnarable",1.0);
        put("summonShadows",1.5);
        put("controlShadows",1.0);
        put("shadowMonster", 1.0);
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
        String task = getEntityData().get(ACTUAL_TASK);
            event.getController().setAnimationSpeed(animationSpeedMap.get(task));
            event.getController().setAnimation(new AnimationBuilder().addAnimation("hunter.animation."+task));
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
            HuntersHP.get(this.level).changeHP(this.getHealth());
            //if there is no victim in world:
            if (SaveVictim.get(this.level).getVictim().equals("novictim") || (DelayBeforeSpawningHunter.get(level).getHunterDelay() - HunterTeleportFormEntity.lifeTime - 5 <= 0 && DelayBeforeSpawningHunter.get(level).getHunterDelay() > -1)) {
                this.disappearInShadows();
            } else {
                    //Is victim here?
                    boolean isVictimHere = false;
                    List<Entity> entities = level.getEntities(this, new AABB(this.getX() - maxDistanceToVictim, this.getY() - maxDistanceToVictim, this.getZ() - maxDistanceToVictim, this.getX() + maxDistanceToVictim, this.getY() + maxDistanceToVictim, this.getZ() + maxDistanceToVictim), EntitySelector.LIVING_ENTITY_STILL_ALIVE);
                    for (int i = 0; i < entities.size() && !isVictimHere; i++) {
                        if (entities.get(i) instanceof Player) {
                            ServerPlayer player = (ServerPlayer) entities.get(i);
                                if (UUID.fromString(SaveVictim.get(this.level).getVictim()).equals(player.getUUID())) {
                                    isVictimHere = true;
                                    //rotation stuff:
                                    Messages.sendToHunter(new PacketSyncVictimToClient(player.getUUID()), this);
                                    EntityAnchorArgument.Anchor pAnchor = EntityAnchorArgument.Anchor.FEET;
                                    Vec3 pTarget = player.getEyePosition();
                                    Vec3 vec3 = pAnchor.apply(this);
                                    if(!getActualTask().equals("shadowMonster") && !getActualTask().equals("summonShadows")) {
                                        this.setYBodyRot((float) (-Math.toDegrees(((float) (Math.atan2(pTarget.x - vec3.x, pTarget.z - vec3.z))))));
                                        this.getLookControl().setLookAt(player);
                                    }
                                    //giving some info for client handling for arrows and so on
                                    setXCoordToAim((float) player.getEyePosition().x);
                                    setYCoordToAim((float) player.getEyePosition().y);
                                    setZCoordToAim((float) player.getEyePosition().z);
                                    //choosing what will Hunter do
                                    if (random.nextBoolean() && getEntityData().get(SHOOTPHASE4) == 10 && player.getEyePosition().distanceTo(new Vec3(getXCoordToAim(), getYCoordToAim(), getZCoordToAim())) < 0.05 && player.getBrightness() <= maxLight) {
                                        boolean flag = false;
                                        for (int u = -1; u < 2 && !flag; u++) {
                                            for (int y = -1; y < 2 && !flag; y++) {
                                                if (level.getBlockState(new BlockPos(player.getBlockX() + u, player.getBlockY() - 1, player.getBlockZ() + y)).getMaterial().blocksMotion())
                                                    flag = true;
                                            }
                                        }
                                        //if player is flying, we want to summon Shadow Monster
                                        if (flag) {
                                            if (random.nextInt(4) == 1) {
                                                setActualTask("shadowMonster");
                                                ShadowMonsterEntity shadowMonster = new ShadowMonsterEntity(ModEntityTypes.SHADOW_MONSTER.get(), level);
                                                shadowMonster.getEntityData().set(ShadowMonsterEntity.OWNER, this.getUUID().toString());
                                                shadowMonster.setPos(player.position());
                                                level.addFreshEntity(shadowMonster);
                                                setGrabUUID(shadowMonster.getUUID().toString());
                                                //else, we would prefer hands
                                            } else {
                                                setActualTask("summonShadows");
                                                ShadowGrabEntity shadowGrabEntity = new ShadowGrabEntity(ModEntityTypes.SHADOW_GRAB.get(), level);
                                                shadowGrabEntity.getEntityData().set(ShadowGrabEntity.OWNER, this.getUUID().toString());
                                                shadowGrabEntity.setPos(player.position());
                                                level.addFreshEntity(shadowGrabEntity);
                                                setGrabUUID(shadowGrabEntity.getUUID().toString());
                                            }
                                        }else {
                                            int count = 0;
                                            boolean isSolidBlockThere = false;
                                            for(int l =0; l < 20 && !isSolidBlockThere; l++){
                                                if(level.getBlockState(new BlockPos(player.getX(), player.getY() - 1 - l, player.getZ())).getMaterial().blocksMotion())
                                                    isSolidBlockThere = true;
                                                else
                                                    count += 1;
                                            }
                                            if(isSolidBlockThere){
                                                setActualTask("shadowMonster");
                                                ShadowMonsterEntity shadowMonster = new ShadowMonsterEntity(ModEntityTypes.SHADOW_MONSTER.get(), level);
                                                shadowMonster.getEntityData().set(ShadowMonsterEntity.OWNER, this.getUUID().toString());
                                                shadowMonster.setPos(player.position().subtract(0,count,0));
                                                level.addFreshEntity(shadowMonster);
                                                setGrabUUID(shadowMonster.getUUID().toString());
                                            }
                                        }
                                    }
                                    //if hands are dead or smth else happened and there is no alive hands-mob, we change the task
                                    if (Objects.equals(getGrabUUID(), "NoGrabEntity") || ((ServerLevel) level).getEntity(UUID.fromString(getGrabUUID())) == null || ((((ServerLevel) level).getEntity(UUID.fromString(getGrabUUID()))) != null && !(((ServerLevel) level).getEntity(UUID.fromString(getGrabUUID()))).isAlive()) && isGrabbing()) {
                                        if (!isShooting()) setActualTask("prepareForShoot");
                                    }
                                    //if it's time to shoot arrow:
                                    if (getEntityData().get(SHOOTPHASE4) == 3) {
                                        // calculations for choosing the position to summon arrow
                                        Vec3 vec31 = EntityAnchorArgument.Anchor.FEET.apply(this);
                                        Vec3 Ptarget = player.position();
                                        double dx = Ptarget.x - vec31.x;
                                        double dz = Ptarget.z - vec31.z;
                                        double xz = Math.sqrt(dx * dx + dz * dz);
                                        double dy = Ptarget.y - (vec31.y + 3.1D);
                                        double cosV = Math.cos((float) (Math.atan2(dy, xz)) * 1.3f);
                                        double xzArmLength = 0.3D + 1.5 * cosV;
                                        double bodyRotAngle = (float) (Math.atan2(dx, dz) + 0.05f);
                                        double arrowXPos = vec31.x + xzArmLength * Math.sin(bodyRotAngle);
                                        double arrowZpos = vec31.z + xzArmLength * Math.cos(bodyRotAngle);
                                        double arrowYpos = vec31.y + 3.4 + 1.5 * Math.sin((float) (Math.atan2(dy, xz)) * 1.3f);
                                        Vec3 arrowPos = new Vec3(arrowXPos, arrowYpos, arrowZpos);

                                        //depending on situation Hunter should decide if he needs to shoot destroying arrow:
                                        boolean isBadCondition = checkConditionForExplosionArrow(level,new BlockPos(arrowXPos, arrowYpos, arrowZpos), new BlockPos(player.position().x, player.getEyeY(), player.position().z));

                                        //if Hunter can see his victim
                                        if (level.clip(new ClipContext(player.getEyePosition(), arrowPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getType() == HitResult.Type.MISS) {
                                            SpeedArrow arrow = new SpeedArrow(ModEntityTypes.SPEED_ARROW.get(), this.level);
                                            arrow.setPos(arrowXPos, arrowYpos, arrowZpos);
                                            Vec3 destination = new Vec3(player.getX() - arrowXPos, player.getY() + 1.5F - arrowYpos, player.getZ() - arrowZpos);
                                            arrow.setOwner(this);
                                            arrow.shoot(player.getX() - arrowXPos, player.getY() + 0.5*destination.length()/maxDistanceToVictim + 1.5F - arrowYpos, player.getZ() - arrowZpos, 7.0f, 0);
                                            this.level.addFreshEntity(arrow);


                                        }
                                        //if there are blocks:
                                        else {
                                            if (!isBadCondition) {
                                                ExplosionArrow arrow = new ExplosionArrow(ModEntityTypes.EXPLOSION_ARROW.get(), level);
                                                arrow.setPos(arrowXPos, arrowYpos, arrowZpos);
                                                Vec3 destination = new Vec3(player.getX() - arrowXPos, player.getY() + 1.5F - arrowYpos, player.getZ() - arrowZpos);
                                                arrow.setOwner(this);
                                                arrow.shoot(player.getX() - arrowXPos, player.getY() + 2.2*destination.length()/maxDistanceToVictim + 1.5F - arrowYpos, player.getZ() - arrowZpos, 3f, 0);
                                                this.level.addFreshEntity(arrow);
                                            } else {
                                                //todo: decide what to do if there are bad blocks
                                            }
                                        }
                                    }
                                }
                            //if player is too near to Hunter:
                            if (player.distanceTo(this) < 6 && !isGrabbing()) {
                                disappearInShadows();
                                //some delay
                                DelayBeforeSpawningHunter.get(level.getServer().overworld()).changeTime(HunterTeleportFormEntity.lifeTime + 5);

                            }
                        }
                    }
                    //creating particles around Hunter's hand if he uses shadow hands
                    if (Objects.equals(getActualTask(), "controlShadows")) {
                        double armLength = 0.3D + 1.5;
                        //some simple vector stuff
                        if (this.cordsForShadowsHunter.size() < 6) {
                            double radius = random.nextDouble(0.15, 0.5);
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
                            arrayList.add(random.nextInt(200));
                            cordsForShadowsHunter.put(new Vec3(this.position().x + lookVector.x + m.x * 0.40 + k.x * 0.2, this.position().y + 2.7 + lookVector.y + k.y * 0.2, this.position().z + lookVector.z + m.z * 0.40 + k.z * 0.2), arrayList);
                        }
                        HashMap map = (HashMap) cordsForShadowsHunter.clone();
                        Iterator<Vec3> iterator = map.keySet().iterator();
                        while (iterator.hasNext()) {
                            Vec3 dotInSpace = iterator.next();
                            ArrayList<Object> list = cordsForShadowsHunter.get(dotInSpace);
                            double radius = (double) list.get(0);
                            Vec3 m = ((Vec3) list.get(1)).scale(radius);
                            Vec3 k = ((Vec3) list.get(2)).scale(radius);
                            for (int h = 0; h < 13; h++) {
                                int timer = (int) list.get(3);
                                Vec3 a = m.scale(Math.cos(timer * Math.PI / 100)).add(k.scale(Math.sin(timer * Math.PI / 100)));
                                list.remove(3);
                                list.add(timer + 1);
                                Vec3 endPosition = dotInSpace.add(a);
                                ((ServerLevel) this.level).sendParticles(new TailBlackParticles.TailParticleData(random.nextDouble(0.03D, 0.05D), random.nextInt(19, 20)),
                                        endPosition.x, endPosition.y, endPosition.z, 1, 0,
                                        0, 0, 0);
                                if (random.nextInt(60) == 37) {
                                    cordsForShadowsHunter.remove(dotInSpace);
                                }
                            }
                        }
                    } else
                        cordsForShadowsHunter.clear();

                    if (!isVictimHere) {
                        this.disappearInShadows();
                        DelayBeforeSpawningHunter.get(level).changeTime(HunterTeleportFormEntity.lifeTime);

                    }

                System.out.println("Task before check: " + getActualTask());

                //now we need to add time for current action and set to 0 all other timers, this is how Hunter task system works(yeap, instead of "Goals" system. I created bicycle

                HashMap<String, EntityDataAccessor<Integer>> map = (HashMap<String, EntityDataAccessor<Integer>>) actionsMap.clone();
                Iterator<String> iteratorForActions = map.keySet().iterator();
                while (iteratorForActions.hasNext()) {
                    String action = iteratorForActions.next();
                    if (Objects.equals(action, getActualTask()))
                        getEntityData().set(actionsMap.get(action), getEntityData().get(actionsMap.get(action)) + 1);
                    else getEntityData().set(actionsMap.get(action), 0);
                }
                //when some task ends, it must trigger another task to start, it is fully individual for each task
                if (getActualTask().equals("prepareForShoot") && getEntityData().get(TIMER_FOR_PREPARING) == 5)
                    setActualTask("shootPhase1");
                if (getActualTask().equals("shootPhase1") && getEntityData().get(SHOOTPHASE1) == 7)
                    setActualTask("shootPhase2");
                if (getActualTask().equals("shootPhase2") && getEntityData().get(SHOOTPHASE2) == 12)
                    setActualTask("shootPhase3");
                if (getActualTask().equals("shootPhase3") && getEntityData().get(SHOOTPHASE3) == 9)
                    setActualTask("shootPhase4");
                if (getActualTask().equals("shootPhase4") && getEntityData().get(SHOOTPHASE4) == 10)
                    setActualTask("shootPhase1");


                if (getActualTask().equals("summonShadows") && getEntityData().get(TIMER_FOR_SUMMONING_SHADOWS) >= ShadowGrabEntity.prepareTime)
                    setActualTask("controlShadows");

                if (getActualTask().equals("shadowMonster") && getEntityData().get(TIMER_FOR_SHADOW_MONSTER) >= ShadowMonsterEntity.lifeTime)
                    setActualTask("controlShadows");

                System.out.println("Task at the end: " + getActualTask());
                //should we rotate Hunter's hands? (For example, while shooting)
                setShouldRotateHandsForShooting(getActualTask().equals("shootPhase4") && getEntityData().get(SHOOTPHASE4) < 8);

            }
        }
        else{
            if (getActualTask().equals("shadowMonster") && getEntityData().get(TIMER_FOR_SHADOW_MONSTER) == 2){
                level.playSound(Minecraft.getInstance().player, this.getX(), this.getY(), this.getZ(), MCSounds.beastUnknownLanguage.get(), SoundSource.VOICE, 0.2f, 1.0f);
            }
        }
    }




private boolean isShooting(){
        return  getActualTask().equals("shootPhase1") || getActualTask().equals("shootPhase2")||getActualTask().equals("shootPhase3")||getActualTask().equals("shootPhase4");
}
    public boolean isGrabbing(){
        return getActualTask().equals("controlShadows") || getActualTask().equals("summonShadows") || getActualTask().equals("shadowMonster");
    }
//does not take magic damage
@Override
public boolean hurt(DamageSource pSource, float pAmount) {
        //when attacked, Hunter will take damage or, with chance, hide in shadows:
        if(!level.isClientSide()) {
            if (pSource.isMagic()) {
                return false;
            } else {
                    int number = random.nextInt(3);
                    if (number == 2 || number == 1) {
                        return super.hurt(pSource, pAmount * 0.1f);
                    }
                    if (number == 0) {
                        this.disappearInShadows();
                        DelayBeforeSpawningHunter.get(level.getServer().overworld()).changeTime(HunterTeleportFormEntity.lifeTime + 5);
                        return false;
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
        //getEntityData().define(TIMER_FOR_FALLING,0);
        getEntityData().define(XCoord, 0f);
        getEntityData().define(ZCoord, 0f);
        getEntityData().define(YCoord, 0f);
        getEntityData().define(TIMER_FOR_SUMMONING_SHADOWS,0);
        getEntityData().define(TIMER_FOR_CONTROLLING_SHADOWS,0);
getEntityData().define(ACTUAL_TASK, "prepareForShoot");
        getEntityData().define(SHOOTPHASE1, 0);
        getEntityData().define(SHOOTPHASE2, 0);
        getEntityData().define(SHOOTPHASE3, 0);
        getEntityData().define(SHOOTPHASE4, 0);
       // getEntityData().define(TIMER_FOR_VULNARABLE, 0);
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

        if(!Objects.equals(getActualTask(), "summonShadows") && !Objects.equals(getActualTask(), "shadowMonster")) {
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
    //bad condition is a situation when there are too solid blocks on the arrow's way.
    public static boolean checkConditionForExplosionArrow(Level level,BlockPos posFrom, BlockPos posTo){
        Iterable<BlockPos> blocksBetween = BlockPos.betweenClosed(posFrom, posTo);
        Iterator<BlockPos> iterator = blocksBetween.iterator();
        //int countForBlocks = 0;
        boolean isBadCondition = false;
        ArrayList<BlockPos> blocks = new ArrayList<>();
        while (iterator.hasNext()) {
            BlockPos block = iterator.next();
            if (!(level.getBlockState(block).isAir())) {
                if (level.getBlockState(block).getMaterial().blocksMotion()  && !blocks.contains(block) ) {
                    blocks.add(block);
                }
                if (level.getBlockState(block).getBlock().getExplosionResistance() >= Blocks.OBSIDIAN.getExplosionResistance()) {
                    isBadCondition = true;
                }
            }
        }
        for (int bl = 0; bl < blocks.size() && !isBadCondition; bl++) {
            BlockPos pos = blocks.get(bl);
            if (!level.getBlockState(pos).isAir() && Math.sqrt(posTo.distToCenterSqr(pos.getX(), pos.getY(), pos.getZ())) > 6)
                isBadCondition = true;
        }
        return isBadCondition;
    }
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }
public static boolean doesHunterHaveVictimClient(Level level){
        return (ClientVictimData.getVictim() != null) && (level.getPlayerByUUID(ClientVictimData.getVictim()) != null);
}
}



package com.way.suslovila.entity.hunter.teleport;

import com.way.suslovila.entity.ShadowCreature;
import com.way.suslovila.particles.TailBlackParticles;
import com.way.suslovila.savedData.SaveVictim;
import com.way.suslovila.savedData.clientSynch.Messages;
import com.way.suslovila.savedData.clientSynch.PacketSyncVictimToClient;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.processor.IBone;

import java.util.*;

public class HunterTeleportFormEntity extends ShadowCreature implements IAnimatable, IAnimationTickable {
private int timer = lifeTime;
public final static int lifeTime = 23;
    private HashMap<Vec3, ArrayList<Object>> cordsForShadowsAroundHand = new HashMap<>();

    private AnimationFactory factory = new AnimationFactory(this);

    public HunterTeleportFormEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = false;
    }

    @Override
    public void baseTick(){
        super.baseTick();
        timer--;
        if(timer <= 0){
            this.discard();

        }
        if(!level.isClientSide()){
            List<Entity> entities = level.getEntities(this, new AABB(this.getX() - 40.0D, this.getY() - 40.0D, this.getZ() - 40.0D, this.getX() + 40.0D, this.getY() + 40.0D, this.getZ() + 40.0D), EntitySelector.LIVING_ENTITY_STILL_ALIVE);
            boolean flag = true;
            for (int i = 0; i < entities.size() && flag; i++) {
                if (entities.get(i) instanceof Player && SaveVictim.get(this.level).getVictim() != null && !SaveVictim.get(this.level).getVictim().equals("novictim") && UUID.fromString(SaveVictim.get(this.level).getVictim()).equals(((Player) entities.get(i)).getUUID())) {
                    flag = false;
                    Messages.sendToHunter(new PacketSyncVictimToClient(UUID.fromString(SaveVictim.get(this.level).getVictim())), this);
                    Player player = level.getPlayerByUUID(UUID.fromString(SaveVictim.get(this.level).getVictim()));
                    if (player != null) {
                        Vec3 pTarget = player.getEyePosition();
                        Vec3 vec3 = EntityAnchorArgument.Anchor.FEET.apply(this);
                        this.setYBodyRot((float) (-Math.toDegrees(((float) (Math.atan2(pTarget.x - vec3.x, pTarget.z - vec3.z))))));
                        this.getLookControl().setLookAt(player);
                    }
                }
            }
            if (flag) {
                Messages.sendToHunter(new PacketSyncVictimToClient(UUID.randomUUID()), this);

            }

//            for(int hl = 0; hl < 2; hl++){
//                if (cordsForShadowsAroundHand.size() < 11) {
//                    double radius = random.nextDouble(0.3, 1.4);
//                    int timer = 0;
//                    Vec3 lookVector = this.getViewVector(0);
//                    Vec3 lookVectorNormal = new Vec3(lookVector.x + random.nextDouble(-2, 2), 0, lookVector.z + random.nextDouble(-2, 2));
//                    Vec3 m = new Vec3(lookVectorNormal.z, 0, -lookVectorNormal.x);
//                    m = m.normalize();
//                    // Vec3 k = lookVectorNormal.cross(m);
//                    Vec3 k = new Vec3(0, -1, 0);
//                    //k = new Vec3(k.x, -Math.abs(k.y), k.z);
//                    ArrayList<Object> arrayList = new ArrayList<Object>();
//                    arrayList.add(radius);
//                    arrayList.add(m);
//                    arrayList.add(k);
//                    arrayList.add(random.nextDouble(0.1D, 0.305D));
//                    arrayList.add(timer);
//                    cordsForShadowsAroundHand.put(new Vec3(this.position().x + random.nextDouble(-1.3, 1.3), this.position().y + random.nextDouble( 1.3), this.position().z + random.nextDouble(-1.3, 1.3)), arrayList);
//                }
//            }
//            HashMap<Vec3, ArrayList> map = (HashMap) cordsForShadowsAroundHand.clone();
//            Iterator<Vec3> iterator = map.keySet().iterator();
//            while (iterator.hasNext()) {
//                Vec3 dotInSpace = iterator.next();
//                ArrayList<Object> list = map.get(dotInSpace);
//                double radius = (double) list.get(0);
//                Vec3 m = ((Vec3) list.get(1)).scale(radius);
//                Vec3 k = ((Vec3) list.get(2)).scale(radius);
//                double particleSize = (double)list.get(3);
//                for (int h = 0; h < 22; h++) {
//                    int timer = (int) list.get(4);
//                    if (timer % 100 == 0 && timer != 0 && random.nextBoolean()) {
//                        cordsForShadowsAroundHand.remove(dotInSpace);
//                        double newRadius = random.nextDouble(0.3, 1.4);
//                        double chis = k.y*Math.cos(timer * Math.PI / 100);
//                        Vec3 newDotInSpace = new Vec3(dotInSpace.x, dotInSpace.y + chis/Math.abs(chis)*(newRadius + radius), dotInSpace.z);
//                        Vec3 newK = new Vec3(0, -k.y, 0);
//                        ArrayList<Object> newList = new ArrayList<>();
//                        newList.add(newRadius);
//                        newList.add(m.normalize());
//                        newList.add(newK.normalize());
//                        newList.add(particleSize);
//                        newList.add(timer+1);
//                        cordsForShadowsAroundHand.put(newDotInSpace, newList);
//                    }
//                    Vec3 a = m.scale(Math.sin(timer * Math.PI / 100)).add(k.scale(Math.cos(timer * Math.PI / 100)));
//                    list.remove(4);
//                    list.add(timer + 1);
//                    Vec3 endPosition = dotInSpace.add(a);
//                    ((ServerLevel) this.level).sendParticles(new TailBlackParticles.TailParticleData(random.nextDouble(particleSize-0.04*particleSize/0.3, particleSize), random.nextInt(13, 14)),
//                            endPosition.x, endPosition.y, endPosition.z, 1, 0,
//                            0, 0, 0);
//                    if (random.nextInt(200) == 37) {
//                        cordsForShadowsAroundHand.remove(dotInSpace);
//                    }
//                }
//            }
            spawnShadowParticles(11, 22, 2, 0.3, 1.4, 0.1,0.305D, 1.3,1.3,0,1.3,1.3,1.3);

        }
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
            event.getController().setAnimationSpeed(1.5);
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.hunterteleportform.teleport", true));
        return PlayState.CONTINUE;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }
//    @Override
//    public Packet<?> getAddEntityPacket() {
//        return NetworkHooks.getEntitySpawningPacket(this);
//    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    public void lookAtVictim(EntityAnchorArgument.Anchor pAnchor, Vec3 pTarget, IBone head) {
        Vec3 vec3 = getEyePosition().add(0,-(3.22D) * tickCount/lifeTime,0);
        double dx = pTarget.x - vec3.x;
        double dz = pTarget.z - vec3.z;
        double xz = Math.sqrt(dx * dx + dz * dz);
        double dy = pTarget.y- vec3.y;
        head.setRotationX(Mth.wrapDegrees((float) (Math.atan2(dy, xz))));
        double angle = (-Math.toDegrees(((float) (Math.atan2(dx, dz)))));
        this.lerpYRot = (float) angle;
        this.setYBodyRot((float) angle);

    }
    @Override
    public int tickTimer() {
        return tickCount;
    }
}


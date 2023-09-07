package com.way.suslovila.entity;

import com.way.suslovila.particles.TailBlackParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ShadowCreature extends PathfinderMob {
    protected HashMap<Vec3, ArrayList<Object>> cordsForShadowsAroundHand = new HashMap<>();
   // private static final EntityDataAccessor<Boolean> SHOULD_SPAWN_PARTICLES = SynchedEntityData.defineId(ShadowCreature.class, EntityDataSerializers.BOOLEAN);
    protected ShadowCreature(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }
    protected void spawnShadowParticles(int maxShadowsAmount,int speed, int wholeIterationAmount,  double minRadius, double maxRadius, double minParticleSize,double maxParticleSize, double minXOffset, double maxXOffset, double minYOffset,double maxYOffset, double minZOffset, double maxZOffset){
        for(int hl = 0; hl < wholeIterationAmount; hl++){
            if (cordsForShadowsAroundHand.size() < maxShadowsAmount) {
                double radius = random.nextDouble(minRadius, maxRadius);
                int timer = 0;
                Vec3 lookVector = this.getViewVector(0);
                Vec3 lookVectorNormal = new Vec3(lookVector.x + random.nextDouble(-2, 2), 0, lookVector.z + random.nextDouble(-2, 2));
                Vec3 m = new Vec3(lookVectorNormal.z, 0, -lookVectorNormal.x);
                m = m.normalize();
                // Vec3 k = lookVectorNormal.cross(m);
                Vec3 k = new Vec3(0, -1, 0);
                //k = new Vec3(k.x, -Math.abs(k.y), k.z);
                ArrayList<Object> arrayList = new ArrayList<Object>();
                arrayList.add(radius);
                arrayList.add(m);
                arrayList.add(k);
                arrayList.add(random.nextDouble(minParticleSize, maxParticleSize));
                arrayList.add(timer);
                cordsForShadowsAroundHand.put(new Vec3(this.position().x + random.nextDouble(-minXOffset, maxXOffset), this.position().y + random.nextDouble(-minYOffset, maxYOffset), this.position().z + random.nextDouble(-minZOffset, maxZOffset)), arrayList);
            }
        }
        HashMap<Vec3, ArrayList> map = (HashMap) cordsForShadowsAroundHand.clone();
        Iterator<Vec3> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            Vec3 dotInSpace = iterator.next();
            ArrayList<Object> list = map.get(dotInSpace);
            double radius = (double) list.get(0);
            Vec3 m = ((Vec3) list.get(1)).scale(radius);
            Vec3 k = ((Vec3) list.get(2)).scale(radius);
            double particleSize = (double)list.get(3);
            for (int h = 0; h < speed; h++) {
                int timer = (int) list.get(4);
                if (timer % 100 == 0 && timer != 0 && random.nextBoolean()) {
                    cordsForShadowsAroundHand.remove(dotInSpace);
                    double newRadius = random.nextDouble(minRadius, maxRadius);
                    double chis = k.y*Math.cos(timer * Math.PI / 100);
                    Vec3 newDotInSpace = new Vec3(dotInSpace.x, dotInSpace.y + chis/Math.abs(chis)*(newRadius + radius), dotInSpace.z);
                    Vec3 newK = new Vec3(0, -k.y, 0);
                    ArrayList<Object> newList = new ArrayList<>();
                    newList.add(newRadius);
                    newList.add(m.normalize());
                    newList.add(newK.normalize());
                    newList.add(particleSize);
                    newList.add(timer+1);
                    cordsForShadowsAroundHand.put(newDotInSpace, newList);
                }
                Vec3 a = m.scale(Math.sin(timer * Math.PI / 100)).add(k.scale(Math.cos(timer * Math.PI / 100)));
                list.remove(4);
                list.add(timer + 1);
                Vec3 endPosition = dotInSpace.add(a);
                ((ServerLevel) this.level).sendParticles(new TailBlackParticles.TailParticleData(random.nextDouble(particleSize-0.04*particleSize/0.3, particleSize), random.nextInt(13, 14)),
                        endPosition.x, endPosition.y, endPosition.z, 1, 0,
                        0, 0, 0);
                if (random.nextInt(300) == 37) {
                    cordsForShadowsAroundHand.remove(dotInSpace);
                }
            }
        }
    }
}

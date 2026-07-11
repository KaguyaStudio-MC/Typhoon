package icu.gensoukyo.typhoon.content.typhoon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import icu.gensoukyo.typhoon.Typhoon;
import icu.gensoukyo.typhoon.common.network.TyphoonSyncMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.UUID;

public class TyphoonEntity extends SavedData {


    public static final Codec<TyphoonEntity> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Codec.BOOL.fieldOf("paused").forGetter(o -> o.paused),
            Codec.DOUBLE.listOf().fieldOf("list").forGetter(o -> List.of(o.x,o.z,o.r,o.v,o.damage,o.vx,o.vz,o.factor,o.hFactor,o.miny,o.growSpeed,o.maxGrownFactor,o.grownFactor,o.baseGrownFactor,o.height)),
            Codec.LONG.fieldOf("lastTime").forGetter(o -> o.lastTime),
            Codec.LONG.fieldOf("lastHurtTime").forGetter(o -> o.lastHurtTime),
            UUIDUtil.CODEC.fieldOf("lockedUUID").forGetter(o -> o.lockedUUID),
            Codec.LONG.fieldOf("lastLockedTime").forGetter(o -> o.lastLockedTime)
    ).apply(ins, TyphoonEntity::new));

    public boolean paused;

    public static final StreamCodec<ByteBuf, TyphoonEntity> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public TyphoonEntity decode(ByteBuf buf) {
            double x = ByteBufCodecs.DOUBLE.decode(buf);
            double z = ByteBufCodecs.DOUBLE.decode(buf);
            double v = ByteBufCodecs.DOUBLE.decode(buf);
            double vx = ByteBufCodecs.DOUBLE.decode(buf);
            double vz = ByteBufCodecs.DOUBLE.decode(buf);
            double damage = ByteBufCodecs.DOUBLE.decode(buf);
            double factor = ByteBufCodecs.DOUBLE.decode(buf);
            double height = ByteBufCodecs.DOUBLE.decode(buf);
            double miny = ByteBufCodecs.DOUBLE.decode(buf);
            double r = ByteBufCodecs.DOUBLE.decode(buf);
            boolean paused = ByteBufCodecs.BOOL.decode(buf);
            double hFactor = ByteBufCodecs.DOUBLE.decode(buf);
            double growSpeed = ByteBufCodecs.DOUBLE.decode(buf);
            double maxGrownFactor = ByteBufCodecs.DOUBLE.decode(buf);
            double baseGrownFactor = ByteBufCodecs.DOUBLE.decode(buf);
            double grownFactor = ByteBufCodecs.DOUBLE.decode(buf);
            long lastTime = ByteBufCodecs.LONG.decode(buf);
            long lastHurtTime = ByteBufCodecs.LONG.decode(buf);
            long lastLockedTime = ByteBufCodecs.LONG.decode(buf);
            UUID lockedUUID = UUIDUtil.STREAM_CODEC.decode(buf);
            return new TyphoonEntity(x, z, v, vx, vz, damage, factor, height, miny, r, paused, hFactor, growSpeed, maxGrownFactor, grownFactor, baseGrownFactor, lastTime, lastHurtTime, lockedUUID, lastLockedTime);
        }

        @Override
        public void encode(ByteBuf buf, TyphoonEntity value) {
            ByteBufCodecs.DOUBLE.encode(buf, value.x);
            ByteBufCodecs.DOUBLE.encode(buf, value.z);
            ByteBufCodecs.DOUBLE.encode(buf, value.v);
            ByteBufCodecs.DOUBLE.encode(buf, value.vx);
            ByteBufCodecs.DOUBLE.encode(buf, value.vz);
            ByteBufCodecs.DOUBLE.encode(buf, value.damage);
            ByteBufCodecs.DOUBLE.encode(buf, value.factor);
            ByteBufCodecs.DOUBLE.encode(buf, value.height);
            ByteBufCodecs.DOUBLE.encode(buf, value.miny);
            ByteBufCodecs.DOUBLE.encode(buf, value.r);
            ByteBufCodecs.BOOL.encode(buf, value.paused);
            ByteBufCodecs.DOUBLE.encode(buf, value.hFactor);
            ByteBufCodecs.DOUBLE.encode(buf, value.growSpeed);
            ByteBufCodecs.DOUBLE.encode(buf, value.maxGrownFactor);
            ByteBufCodecs.DOUBLE.encode(buf, value.baseGrownFactor);
            ByteBufCodecs.DOUBLE.encode(buf, value.grownFactor);
            ByteBufCodecs.LONG.encode(buf, value.lastTime);
            ByteBufCodecs.LONG.encode(buf, value.lastHurtTime);
            ByteBufCodecs.LONG.encode(buf, value.lastLockedTime);
            UUIDUtil.STREAM_CODEC.encode(buf,value.lockedUUID);
        }
    };

    public static TyphoonEntity INSTANCE;


    public static final SavedDataType<TyphoonEntity> ID = new SavedDataType<>(
            Typhoon.id("typhoon"),
            TyphoonEntity::new,
            CODEC
    );


    public double x, z;

    public final double v;
    public double vx, vz;

    public final double damage;
    public final double factor, hFactor;
    public final double growSpeed, maxGrownFactor, baseGrownFactor;
    public double grownFactor;

    public final double height, miny;

    public final double r;

    private long lastTime;
    private long lastHurtTime;

    public UUID lockedUUID;
    public long lastLockedTime;

    public TyphoonEntity(double x, double z, double v, double vx, double vz, double damage, double factor, double height, double miny, double r, boolean paused, double hFactor, double growSpeed, double maxGrownFactor, double grownFactor, double baseGrownFactor,long lastTime, long lastHurtTime, UUID lockedUUID, long lastLockedTime) {
        this.x = x;
        this.z = z;
        this.v = v;
        this.vx = vx;
        this.vz = vz;
        this.damage = damage;
        this.factor = factor;
        this.height = height;
        this.miny = miny;
        this.r = r;
        this.hFactor = hFactor;
        this.growSpeed = growSpeed;
        this.maxGrownFactor = maxGrownFactor;
        this.paused = paused;
        this.grownFactor = grownFactor;
        this.baseGrownFactor = baseGrownFactor;
        this.lastTime = lastTime;
        this.lastHurtTime = lastHurtTime;
        this.lockedUUID = lockedUUID;
        this.lastLockedTime = lastLockedTime;
    }

    public TyphoonEntity(double v, double damage, double factor, double height, double miny, double r, boolean paused, double hFactor, double growSpeed, double maxGrownFactor, double baseGrownFactor) {
        this.v = v;
        this.damage = damage;
        this.factor = factor;
        this.height = height;
        this.miny = miny;
        this.r = r;
        this.hFactor = hFactor;
        this.growSpeed = growSpeed;
        this.maxGrownFactor = maxGrownFactor;
        this.lastTime = System.currentTimeMillis();
        this.lastHurtTime = System.currentTimeMillis();
        this.paused = paused;
        this.grownFactor = baseGrownFactor;
        this.baseGrownFactor = baseGrownFactor;
        this.lockedUUID = UUID.randomUUID();
    }

    public TyphoonEntity() {
        this.factor = 0;
        this.height = 0;
        this.miny = 0;
        this.r = 0;
        this.damage = 0;
        this.v = 0;
        this.hFactor = 0;
        this.lastTime = System.currentTimeMillis();
        this.growSpeed = 0;
        this.maxGrownFactor = 0;
        this.paused = true;
        this.grownFactor = 0;
        this.baseGrownFactor = 0;
    }

    public TyphoonEntity(boolean paused,List<Double> doubles,long lastTime, long lastHurtTime, UUID lockedUUID, long lastLockedTime) {
        this.x = doubles.get(0);
        this.z = doubles.get(1);
        this.r = doubles.get(2);
        this.v = doubles.get(3);
        this.damage = doubles.get(4);
        this.vx = doubles.get(5);
        this.vz = doubles.get(6);
        this.factor = doubles.get(7);
        this.hFactor = doubles.get(8);
        this.miny = doubles.get(9);
        this.growSpeed = doubles.get(10);
        this.maxGrownFactor = doubles.get(11);
        this.grownFactor = doubles.get(12);
        this.baseGrownFactor = doubles.get(13);
        this.height = doubles.get(14);
        this.paused = paused;
        this.lastTime = lastTime;
        this.lastHurtTime = lastHurtTime;
        this.lockedUUID = lockedUUID;
        this.lastLockedTime = lastLockedTime;
    }

    public void setPos(double x, double y) {
        this.x = x;
        this.z = y;
    }

    public void tick(Level level) {
        if (lastTime == 0) lastTime = System.currentTimeMillis();
        if (paused) return;

        long now = System.currentTimeMillis();

        Player locked;

        if(now-lastLockedTime>60000) {
            locked = level.getNearestPlayer(x, 0, z, Double.MAX_VALUE, (e) -> {
                if(e instanceof Player player){
                    if (!player.gameMode().isSurvival()) {
                        return false;
                    }
                    if (player.position().distanceTo(new Vec3(x,100,z))<0.2*r){
                        return false;
                    }
                    return true;
                }
                return false;
            });
            if (locked != null) {
                lockedUUID = locked.getUUID();
                lastLockedTime= now;

                try{
                    PlayerList playerList = ServerLifecycleHooks.getCurrentServer().getPlayerList();
                    playerList.broadcastSystemMessage(Component.literal("巴威锁定了 [").append(locked.getDisplayName()).append(Component.literal("] ！")), false);
                }catch (Exception _){
                }
            }
        }else{
            locked=level.getPlayerByUUID(lockedUUID);
        }
        if (locked != null) {
            Vec3 vec3 = new Vec3(x, locked.position().y, z).vectorTo(locked.position()).normalize();
            vx = vec3.x * v * grownFactor;
            vz = vec3.z * v * grownFactor;
        }



        double dt = (now - lastTime) / 1000.0;

        this.x += vx * dt;
        this.z += vz * dt;

        lastTime = now;
        this.setDirty();
        Iterable<Entity> allEntities = null;
        if (level instanceof ServerLevel serverLevel) {
            allEntities = serverLevel.getAllEntities();
        }
        if (level instanceof ClientLevel clientLevel) {
            allEntities = clientLevel.entitiesForRendering();
        }
        if (allEntities == null) return;

        allEntities.forEach(entity -> {

            Vec3 wind = getFactorAtPos(entity.position());

            if (entity instanceof Player player
                    && player.isShiftKeyDown()) {

                wind = new Vec3(
                        wind.x * 0.9,
                        wind.y * 0.6,
                        wind.z * 0.9
                );
            }

            double resistance = 1.0;

            if (entity instanceof Player player) {
                resistance = 0.5;
                if (!player.gameMode().isSurvival()) {
                    return;
                }
            }


            wind = wind.scale(resistance);


            Vec3 velocity = entity.getDeltaMovement();

            velocity = velocity.add(
                    wind.subtract(velocity)
                            .scale(0.15)
            );

            entity.setDeltaMovement(velocity);

            double s = velocity.distanceTo(Vec3.ZERO);
            if (s > 2 && this.lastHurtTime + 2000 < now) {
                if (entity instanceof LivingEntity entity1) {
                    entity1.hurt(level.damageSources().windCharge(entity1, null), (float) ((s / 2) * damage));
                    this.lastHurtTime = now;
                }
            }
        });

        if (grownFactor < maxGrownFactor) {
            grownFactor += growSpeed;
        } else {
            grownFactor = maxGrownFactor;
        }

        if (level instanceof ServerLevel) {
            PacketDistributor.sendToAllPlayers(new TyphoonSyncMessage(this));
        }
    }

    static double smoothstep(double edge0, double edge1, double x) {
        x = Math.clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
        return x * x * (3.0 - 2.0 * x);
    }

    public Vec3 getFactorAtPos(Vec3 pos) {
        if (paused) return Vec3.ZERO;

        // 低于作用高度没有风
        if (pos.y <= miny) {
            return Vec3.ZERO;
        }

        double dx = pos.x - x;
        double dz = pos.z - z;

        double dist = Math.sqrt(dx * dx + dz * dz);

        // 超出影响范围
        if (dist >= r * 2 || dist < 1e-6) {
            return Vec3.ZERO;
        }

        // ===========================
        // 高度衰减
        // ===========================

        double t2 = Math.clamp((pos.y - miny) / height, 0.0, 1.0);
        double hfactor = smoothstep(0.0, 0.5, t2) * (1.0 - smoothstep(0.5, 1.0, t2));

        // ===========================
        // 单位向量
        // ===========================

        double nx = dx / dist;
        double nz = dz / dist;

        // 切向（逆时针）
        double tx = -nz;
        double tz = nx;

        // 径向（指向中心）
        double rx = -nx;
        double rz = -nz;

        // ===========================
        // 风强（眼墙最强）
        // ===========================

        double u = dist / r;

        // 眼墙
        double wall =
                Math.exp(-Math.pow((u - 0.38) / 0.08, 2));

        // 外围
        double outer =
                Math.exp(-Math.pow((u - 0.70) / 0.25, 2));

        double strength = wall + outer * 0.35;

        // ===========================
        // 基础风
        // ===========================

        double rotate = strength;
        double inflow = strength * 0.25;

        double fx = tx * rotate + rx * inflow;
        double fz = tz * rotate + rz * inflow;

        double fy = strength * hfactor * hFactor;

        // ===========================
        // 阵风
        // ===========================

        double t = System.currentTimeMillis() * 0.001;

        double gustX =
                Math.sin(pos.x * 0.012 + t * 0.8)
                        + 0.5 * Math.sin(pos.z * 0.021 - t * 1.1)
                        + 0.3 * Math.sin((pos.x + pos.z) * 0.017 + t * 0.6);

        double gustZ =
                Math.sin(pos.z * 0.015 - t * 0.7)
                        + 0.5 * Math.sin(pos.x * 0.019 + t * 0.9)
                        + 0.3 * Math.sin((pos.x - pos.z) * 0.014 - t * 0.5);

        double gustLen = Math.sqrt(gustX * gustX + gustZ * gustZ);

        if (gustLen > 1e-6) {
            gustX /= gustLen;
            gustZ /= gustLen;
        }

        double gustStrength =
                (0.5 + 0.5 * Math.sin(t * 0.15))
                        * 0.15
                        * strength;

        fx += gustX * gustStrength;
        fz += gustZ * gustStrength;

        // ===========================
        // 局部湍流
        // ===========================

        double noise =
                Math.sin(pos.x * 0.15)
                        * Math.cos(pos.z * 0.13)
                        * Math.sin(t * 0.7);

        double angle = noise * Math.PI * 2.0;

        fx += Math.cos(angle) * strength * 0.05;
        fz += Math.sin(angle) * strength * 0.05;

        // ===========================
        // 总倍率
        // ===========================

        double scale = factor * hfactor * 1e4 * grownFactor;

        Vec3 vec3 = new Vec3(
                fx * scale,
                fy * scale,
                fz * scale
        );

        if (vec3.distanceTo(Vec3.ZERO) > 20) {
            vec3 = vec3.normalize().scale(20);
        }
        return vec3;
    }
}

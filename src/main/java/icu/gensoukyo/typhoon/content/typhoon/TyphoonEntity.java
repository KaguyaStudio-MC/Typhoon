package icu.gensoukyo.typhoon.content.typhoon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import icu.gensoukyo.typhoon.Typhoon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.phys.Vec3;

public class TyphoonEntity extends SavedData {

    public boolean paused;

    public static final StreamCodec<ByteBuf, TyphoonEntity> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public TyphoonEntity decode(ByteBuf buf) {
            double x = ByteBufCodecs.DOUBLE.decode(buf);
            double y = ByteBufCodecs.DOUBLE.decode(buf);
            double v = ByteBufCodecs.DOUBLE.decode(buf);
            double vx = ByteBufCodecs.DOUBLE.decode(buf);
            double vy = ByteBufCodecs.DOUBLE.decode(buf);
            double factor = ByteBufCodecs.DOUBLE.decode(buf);
            double height = ByteBufCodecs.DOUBLE.decode(buf);
            double miny = ByteBufCodecs.DOUBLE.decode(buf);
            double r = ByteBufCodecs.DOUBLE.decode(buf);
            return new TyphoonEntity(x, y,v, vx, vy, factor, height, miny, r);
        }

        @Override
        public void encode(ByteBuf buf, TyphoonEntity value) {
            ByteBufCodecs.DOUBLE.encode(buf, value.x);
            ByteBufCodecs.DOUBLE.encode(buf, value.y);
            ByteBufCodecs.DOUBLE.encode(buf, value.v);
            ByteBufCodecs.DOUBLE.encode(buf, value.vx);
            ByteBufCodecs.DOUBLE.encode(buf, value.vy);
            ByteBufCodecs.DOUBLE.encode(buf, value.factor);
            ByteBufCodecs.DOUBLE.encode(buf, value.height);
            ByteBufCodecs.DOUBLE.encode(buf, value.miny);
            ByteBufCodecs.DOUBLE.encode(buf, value.r);
            ByteBufCodecs.VAR_LONG.encode(buf, value.lastTime);
        }
    };

    public static TyphoonEntity INSTANCE;

    public static final Codec<TyphoonEntity> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Codec.DOUBLE.fieldOf("x").forGetter(o -> o.x),
            Codec.DOUBLE.fieldOf("y").forGetter(o -> o.y),
            Codec.DOUBLE.fieldOf("v").forGetter(o -> o.v),
            Codec.DOUBLE.fieldOf("vx").forGetter(o -> o.vx),
            Codec.DOUBLE.fieldOf("vy").forGetter(o -> o.vy),
            Codec.DOUBLE.fieldOf("factor").forGetter(o -> o.factor),
            Codec.DOUBLE.fieldOf("height").forGetter(o -> o.height),
            Codec.DOUBLE.fieldOf("miny").forGetter(o -> o.miny),
            Codec.DOUBLE.fieldOf("r").forGetter(o -> o.r)
    ).apply(ins, TyphoonEntity::new));

    public static final SavedDataType<TyphoonEntity> ID = new SavedDataType<>(
            Typhoon.id("typhoon"),
            TyphoonEntity::new,
            CODEC
    );


    private double x,y;

    private final double v;
    private double vx,vy;

    private final double factor;

    private final double height,miny;

    private final double r;

    private long lastTime;

    public TyphoonEntity(double x, double y, double v, double vx, double vy, double factor, double height, double miny, double r) {
        this.x = x;
        this.y = y;
        this.v = v;
        this.vx = vx;
        this.vy = vy;
        this.factor = factor;
        this.height = height;
        this.miny = miny;
        this.r = r;
        this.lastTime = System.currentTimeMillis();
        this.paused = true;
    }

    public TyphoonEntity(double v, double factor, double height, double miny, double r) {
        this.v = v;
        this.factor = factor;
        this.height = height;
        this.miny = miny;
        this.r = r;
        this.lastTime = System.currentTimeMillis();
        this.paused = true;
    }

    public TyphoonEntity() {
        this.factor = 0;
        this.height = 0;
        this.miny = 0;
        this.r = 0;
        this.v = 0;
        this.lastTime = System.currentTimeMillis();
        this.paused = true;
    }

    public void setPos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void tick(ServerLevel level) {
        if (paused) return;

        Player nearestPlayer = level.getNearestPlayer(x, 0, y, Double.MAX_VALUE, true);
        if(nearestPlayer !=null){
            Vec3 vec3 = new Vec3(x, 0, y).vectorTo(nearestPlayer.position()).normalize();
            vx = vec3.x * factor;
            vy = vec3.z * factor;
        }


        long now = System.currentTimeMillis();

        double dt = (now - lastTime) / 1000.0;

        this.x += vx * dt;
        this.y += vy * dt;

        lastTime = now;
        this.setDirty();

        Iterable<Entity> allEntities = level.getAllEntities();

        allEntities.forEach(entity -> {

            Vec3 wind = getFactorAtPos(entity.position());

            if(entity instanceof Player player
                    && player.isShiftKeyDown()) {

                wind = new Vec3(
                        wind.x * 0.3,
                        wind.y * 0.1,
                        wind.z * 0.3
                );
            }

            double resistance = 1.0;

            if(entity instanceof Player)
                resistance = 0.5;

            wind = wind.scale(resistance);


            Vec3 velocity = entity.getDeltaMovement();

            velocity = velocity.add(
                    wind.subtract(velocity)
                            .scale(0.15)
            );

            entity.setDeltaMovement(velocity);
        });
    }

    public Vec3 getFactorAtPos(Vec3 pos) {
        if(paused) return Vec3.ZERO;

        // 低于作用高度没有风
        if (pos.y <= miny) {
            return Vec3.ZERO;
        }

        double dx = pos.x - x;
        double dz = pos.z - y;

        double dist = Math.sqrt(dx * dx + dz * dz);

        // 超出影响范围
        if (dist >= r || dist < 1e-6) {
            return Vec3.ZERO;
        }

        // ===========================
        // 高度衰减
        // ===========================

        double hFactor = Math.min(1.0, (pos.y - miny) / height);

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

        double fy = strength * 0.35;

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

        double scale = factor * hFactor;

        return new Vec3(
                fx * scale,
                fy * scale,
                fz * scale
        );
    }
}

package de.lyriaserver.kartenspiele.classes;

import org.bukkit.block.Block;

import java.util.Objects;
import java.util.UUID;

public class BlockPos {
    private final UUID worldUid;
    private final int x;
    private final int y;
    private final int z;

    public BlockPos(Block block) {
        this(block.getWorld().getUID(), block.getX(), block.getY(), block.getZ());
    }

    public BlockPos(UUID worldUid, int x, int y, int z) {
        this.worldUid = worldUid;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockPos blockPos = (BlockPos) o;
        return x == blockPos.x && y == blockPos.y && z == blockPos.z && Objects.equals(worldUid, blockPos.worldUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldUid, x, y, z);
    }
}

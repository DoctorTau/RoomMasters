using UnityEngine;

namespace FurnitureStruct
{
    public struct StartPosition
    {
        public Vector3 position;
        public Quaternion rotation;

        public Vector2 Cells;

        public StartPosition(Vector3 position, Quaternion rotation, Vector2 Cells)
        {
            this.position = position;
            this.rotation = rotation;
            this.Cells = Cells;
        }
    }
}
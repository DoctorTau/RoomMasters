using UnityEngine;

namespace FurnitureStruct
{
    public struct StartPosition
    {
        public Vector3 position;
        public Quaternion rotation;

        public StartPosition(Vector3 position, Quaternion rotation)
        {
            this.position = position;
            this.rotation = rotation;
        }
    }
}
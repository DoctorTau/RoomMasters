using UnityEngine;
using UnityEngine.UI;
using Assets.Scripts.Classes;

namespace Assets.Scripts
{
    internal class ObjectsMenuButtonFabric : MonoBehaviour
    {
        private static float imageWidth = 275f;
        private static float imageHeight = 275f;
        public static GameObject Create(string buttonImagePath, string objectPrefabPath, Floor floor, string objectName = "New object")
        {
            GameObject gameObject = new GameObject();
            CanvasRenderer cr = gameObject.AddComponent<CanvasRenderer>();

            Image image = gameObject.AddComponent<Image>();
            image.sprite = Resources.Load<Sprite>(buttonImagePath);
            image.rectTransform.sizeDelta = new Vector2(imageWidth, imageHeight);

            ObjectsMenuButton button = gameObject.AddComponent<ObjectsMenuButton>();
            button.ObjectPrefabPath = objectPrefabPath;
            button.floor = floor;
            button.ObjectName = objectName;
            
            RectTransform rt = gameObject.GetComponent<RectTransform>();
            rt.position = Vector3.zero;
            rt.localPosition = Vector3.zero;
            rt.anchoredPosition = Vector3.zero;

            LayoutElement layoutElement = gameObject.AddComponent<LayoutElement>();

            return gameObject;
        }
    }
}

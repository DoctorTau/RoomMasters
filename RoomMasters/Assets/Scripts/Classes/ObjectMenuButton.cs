using UnityEngine;
using UnityEngine.UI;
using UnityEngine.Events;
using UnityEngine.EventSystems;
using UnityEngine.Serialization;

namespace Assets.Scripts.Classes
{
    internal class ObjectsMenuButton : MonoBehaviour, IPointerDownHandler, IPointerUpHandler, IPointerClickHandler
    {
        public string ObjectPrefabPath;
        public string ObjectName = "New object";

        private bool pointerDown;
        private bool pointerUpWaitMode;
        private float pointerDownTimer;
        private float requiredHoldTime = 0.3f;

        public UnityEvent onFastClick = new UnityEvent();
        public UnityEvent onLongClick = new UnityEvent();

        public Floor floor;

        public void OnPointerDown(PointerEventData eventData) => pointerDown = true;
        public void OnPointerUp(PointerEventData eventData) => pointerDown = false;

        public void OnPointerClick(PointerEventData eventData)
        {
            floor.CreateNewObject(Resources.Load(ObjectPrefabPath) as GameObject);
        }

        void Start()
        {
            onFastClick.AddListener(() => Debug.Log("button fast click detected"));
            onLongClick.AddListener(() => Debug.Log("button long click detected"));
            pointerUpWaitMode = false;
        }
        void Update()
        {
            if (pointerUpWaitMode)
            {
                if (pointerDown)
                {
                    pointerDownTimer += Time.deltaTime;
                }
                else
                {
                    if (pointerDownTimer >= requiredHoldTime)
                    {
                        if (onLongClick != null)
                            onLongClick.Invoke();
                    }
                    else
                    {
                        if (onFastClick != null)
                            onFastClick.Invoke();
                    }
                    pointerUpWaitMode = false;
                    Reset();
                }
            }
            else
            {
                pointerUpWaitMode = pointerDown;
            }
        }

        private void Reset()
        {
            pointerDown = false;
            pointerDownTimer = 0;
        }

        public void SetTime(float secs)
        {
            requiredHoldTime = secs;
        }
    }
}

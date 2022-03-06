using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Floar : MonoBehaviour
{
    public Vector2Int GridSize = new Vector2Int(10, 10);

    private Furniture[,] grid;
    private Furniture selectedObject;
    private Camera mainCamera;
    bool dragging = false;

    private void Awake()
    {
        grid = new Furniture[GridSize.x, GridSize.y];
        mainCamera = Camera.main;
    }

    public void CreatNewObject(Furniture furniturePrehub)
    {
        if (selectedObject != null)
            Destroy(selectedObject);

        selectedObject = Instantiate(furniturePrehub);
    }

    // Update is called once per frame
    void Update()
    {
        if (selectedObject != null)
        {
            Touch touch = Input.touches[0];
            Vector3 positionOfTouch = touch.position;

            var groundPlane = new Plane(Vector3.up, Vector3.zero);

            if (groundPlane.Raycast((Ray)mainCamera.ScreenPointToRay(positionOfTouch), out float position))
            {
                Vector3 worldPosition = ((Ray)mainCamera.ScreenPointToRay(positionOfTouch)).GetPoint(position);

                worldPosition.x = Mathf.RoundToInt(worldPosition.x);
                worldPosition.z = Mathf.RoundToInt(worldPosition.z);
                selectedObject.transform.position = worldPosition;
            }
        }

    }
}

package Semantic;

import java.awt.Color;

import javax.swing.JTextArea;

import Main.Draw;
import Main.Point;
import Parser.ParserImp;
import Parser.TreeNode;

public class SemanticImp extends ParserImp
{
	public double Oringin_x;
	public double Oringin_y;
	public double Scale_x;
	public double Scale_y;
	public double rot_angle;
	public Draw draw;
	
	public SemanticImp(Draw draw)
	{
		super();                       //�������ޣ��̳��඼��Ĭ�Ϸ��ʸ�����޲ι��캯�����������й��캯���������޲εģ��������д�޲ι��캯�������򱨴�
		Oringin_x = 0;
		Oringin_y = 0;
		Scale_x = 1;
		Scale_y = 1;
		rot_angle = 0;
		this.draw = draw;
	}

	public Double GetTreeValue(TreeNode root)                  //��ȡ�﷨���ϵ�ֵ
	{
		if (root == null)
		{
			return 0.0;
		}
		switch (root.OpCode)
		{
		case PLUS:
			return GetTreeValue(root.case_operator.left) + GetTreeValue(root.case_operator.right);
		case MINUS:
			return GetTreeValue(root.case_operator.left) - GetTreeValue(root.case_operator.right);
		case MUL:
			return GetTreeValue(root.case_operator.left) * GetTreeValue(root.case_operator.right);
		case DIV:
			return GetTreeValue(root.case_operator.left) / GetTreeValue(root.case_operator.right);
		case POWER:
			return Math.pow(GetTreeValue(root.case_operator.left), GetTreeValue(root.case_operator.left));
		case FUNC:
			return root.case_func.func.calculate(GetTreeValue(root.case_func.child));
		case CONST_ID:
			return root.case_const;
		case T:
			return root.case_parmPtr.getA();
		default:
			return 0.0;
		}
	}
	
	public void CalCoord(TreeNode hor_ptr,TreeNode ver_ptr,Main.Point point) //����������ֵ�����Ȼ�ȡ����ֵ��Ȼ���������任
	{
		double x_val;
		double x_temp;
		double y_val;
		
		x_val = GetTreeValue(hor_ptr);   //������ԭʼ����
		y_val = GetTreeValue(ver_ptr);
		
		x_val *= Scale_x;				//���б任
		y_val *= Scale_y;
		
		x_temp = x_val * Math.cos(rot_angle) + y_val * Math.sin(rot_angle);
		y_val = y_val * Math.cos(rot_angle) - x_val * Math.sin(rot_angle);
		x_val = x_temp;                 //��ת�任
		
		x_val += Oringin_x;				//ƽ�Ʊ任
		y_val += Oringin_y;
		
		point.setX(x_val);
		point.setY(y_val);
	}
	
	public void DeleteTree(TreeNode root)
	{
		if (root == null)
		{
			return;
		}
		switch (root.OpCode)
		{
		case PLUS: 						//�������ӵ��ڲ��ڵ�
		case MINUS:
		case MUL:
		case DIV:
		case POWER:
			DeleteTree(root.case_operator.left);
			DeleteTree(root.case_operator.right);
			break;
		case FUNC:						//һ�����ӵ��ڲ��ڵ�
			DeleteTree(root.case_func.child);
			break;
		default:						//Ҷ�ӽڵ�
			break;
		}
	}
	
	public void DrawLoop(double start_val,double end_val,double step_val,TreeNode x_ptr,TreeNode y_ptr)
	{

		double x_val = 0;
		double y_val = 0;
		Point point = new Point(x_val,y_val);
		
		for(parameter.setA(start_val); parameter.getA() <= end_val; parameter.setA(parameter.getA() + step_val))
		{
			CalCoord(x_ptr, y_ptr, point);
			draw.draw(point, line_color);
		}
		
        JTextArea textField2 = new JTextArea("�༭");
//        textField2.setBackground(Color.pink);
        textField2.setOpaque(false);
        textField2.setBounds((int)point.getX(), (int)point.getY(), 150, 20);
        draw.add(textField2);
		
	}

	@Override
	public void OriginStatement()
	{		
		super.OriginStatement();
		Oringin_x = GetTreeValue(x_ptr);
		DeleteTree(x_ptr);
		Oringin_y = GetTreeValue(y_ptr);
		DeleteTree(y_ptr);
	}

	@Override
	public void RotStatement()
	{	
		super.RotStatement();
		rot_angle = GetTreeValue(angle_ptr);
		DeleteTree(angle_ptr);
	}

	@Override
	public void ScaleStatement()
	{	
		super.ScaleStatement();
		Scale_x = GetTreeValue(x_ptr);
		DeleteTree(x_ptr);
		Scale_y = GetTreeValue(y_ptr);
		DeleteTree(y_ptr);
	}

	@Override
	public void ForStatement()
	{
		double start_val = 0;
		double end_val = 0;
		double step_val = 0;
		super.ForStatement();
		
		start_val = GetTreeValue(start_ptr);
		end_val = GetTreeValue(end_ptr);
		step_val = GetTreeValue(step_ptr);
			
		DrawLoop(start_val, end_val, step_val, x_ptr, y_ptr);

		DeleteTree(start_ptr);
		DeleteTree(end_ptr);
		DeleteTree(step_ptr);
		DeleteTree(x_ptr);
		DeleteTree(y_ptr);
		
	}
	
	
}




